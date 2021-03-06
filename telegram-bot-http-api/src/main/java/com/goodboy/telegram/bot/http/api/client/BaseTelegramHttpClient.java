/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.http.api.client;

import com.goodboy.telegram.bot.http.api.client.adapter.Callback;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapterCallback;
import com.goodboy.telegram.bot.http.api.client.configuration.TelegramApiConfiguration;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpRequestTypeBasedHandler;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.api.platform.entry.Uploading;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;


/**
 * Базовый клиент для работы с Telegram API
 * <p>
 * Разобьем URL для запроса к Telegram API на части для
 * декомпозиции динамических частей и обработчиков, связанных
 * с ними:
 * <p>
 * [(1) http method][(2) host][(3) type][(4) token][(5) method]
 * <p>
 * (1) GET, POST, MULTIPART - тип http запроса
 * (2) HOST                 - хост
 * (3) COMMAND, FILE        - тип запроса: команда, получение файла и т.д.
 * (4) TOKEN                - токен, уникальный идентификатор бота
 * (5) METHOD               - название метода (зависит типа запроса)
 * <p>
 * Пример ссылок:
 * <p>
 * 1) Запрос на получение файла https://api.telegram.org/file/bot<token>/<file_path>
 * <p>
 * (1) тип http запроса                                 = MULTIPART
 * (2) хост                                             = api.telegram.org
 * (3) тип запроса: команда, получение файла и т.д.     = FILE (/file)
 * (4) токен, уникальный идентификатор бота             = bot<token>
 * (5) название метода (зависит типа запроса)           = <file_path>
 * <p>
 * 2) Запрос на установку webhook https://api.telegram.org/bot<token>/setWebhook
 * <p>
 * (1) тип http запроса                                 = POST
 * (2) хост                                             = api.telegram.org
 * (3) тип запроса: команда, получение файла и т.д.     = COMMAND (пусто)
 * (4) токен, уникальный идентификатор бота             = bot<token>
 * (5) название метода (зависит типа запроса)           = setWebhook
 * <p>
 * Таким образом клиент должен состоять из 5-ти динамичных частей:
 * <p>
 * (1) Адаптер для http клиентов (сторонних библиотек) которые могли обработать все необходимые типы запросов
 * (2) Интерфейс по получению актуального host:port - базовая имплементация, хардкод значения
 * (3) Набор обработчиков в зависимости от типа запроса - могут накладывать дополнительные этапы обработки ответного сообщения от telegram
 * (4) Токенизатор - динамическое получение токена, из контекста
 * (5) Обработчик на получение имени метода
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class BaseTelegramHttpClient implements TelegramHttpClient {


    // Адаптер для отправки азпросов
    private final HttpClientAdapter adapter;

    // Набор настроек для клиента
    private final TelegramApiConfiguration configuration;

    // Адаптер для http клиентов (сторонних библиотек) которые могли обработать все необходимые типы запросов
    private final Map<Request.HttpMethod, HttpClientAdapterCallback> mappers;

    //  Обработчик на получение имени метода
    private final Map<RequestType, HttpRequestTypeBasedHandler> handlers;

    // Токенизатор - динамическое получение токена, из контекста
    private final TelegramApiTokenResolver tokenResolver;

    public BaseTelegramHttpClient(
            @Nonnull HttpClientAdapter adapter,
            @Nonnull TelegramApiConfiguration configuration,
            @Nonnull List<HttpClientAdapterCallback> mappers,
            @Nonnull List<HttpRequestTypeBasedHandler> handlers,
            @Nonnull TelegramApiTokenResolver tokenResolver
    ) {
        this.adapter = adapter;
        this.configuration = configuration;
        this.mappers = mappers.stream().collect(Collectors.toMap(HttpClientAdapterCallback::method, m -> m));
        this.handlers = handlers.stream().collect(Collectors.toMap(HttpRequestTypeBasedHandler::type, h -> h));
        this.tokenResolver = tokenResolver;
    }

    @Override
    public <T, V> TelegramCoreResponse<T> send(@NotNull Request<V> request) {
        return process(request);
    }

    @Override
    public <T, V> CompletableFuture<TelegramCoreResponse<T>> sendAsync(Request<V> request) {
        throw new UnsupportedOperationException();
    }

    private <T, V> TelegramCoreResponse<T> process(Request<V> request) {
        final @NonNull Request.CallMethod method = request.callMethod();

        // 1. Получаем урл для формирования запроса без токена
        final String tokenFreeUrl = createTokenFreeUrl(method);

        // 2. Определяем метод отправки запроса GET, POST, MULTIPART
        final Request.HttpMethod type = method.desireHttpMethod().orElseGet(() -> getHttpMethodByContentType(request.payload()));

        // 3. Формируем UniTypeRequest для передачи в адаптер
        final Callback  function = mappers.get(type).callback(adapter, format(tokenFreeUrl, request.token().orElseGet(tokenResolver)), request.payload().orElse(null));

        // 4. Получим результат вызова
        final TelegramHttpResponse response = function.get();

        if (configuration.throwExceptionOnNon2XXHttpCode(response.getStatusCode())) {
            if(log.isDebugEnabled() && response.getBody() != null)
                log.debug("request for telegram api failed with message:" + new String(response.getBody()));
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.NOT_200_HTTP_STATUS_CODE, "status code " + response.getStatusCode());
        }

        // 5. Обработчик на получение имени метода
        return handlers.get(method.type()).handleHttpAdapterResponse(response, method.desireReturnType());
    }

    private String createTokenFreeUrl(Request.CallMethod method) {
        final String url = configuration.getTelegramApiUrl()
                + method.type().getPath()
                + "/bot%s/"
                + method.name();

        if (log.isDebugEnabled())
            log.debug("created http request's url { url = {} }", format(url, "<token>"));

        return url;
    }

    private <V> Request.HttpMethod getHttpMethodByContentType(V payload) {
        // 1) если нет полей => GET
        if(payload == null)
            return Request.HttpMethod.GET;

        List<Uploading> uploads;
        // 2) если нет аплоудов или они не требуют загрузки файла со стороны клиента => POST
        uploads = Arrays.stream(payload.getClass().getDeclaredFields())
                .filter(field -> Uploading.class.isAssignableFrom(field.getType()))
                .map(field -> (Uploading) getFieldValue(payload, field))
                .filter(Objects::nonNull)
                .collect(toList());

        if(uploads
                .stream()
                .allMatch(upload -> upload.fileId() != null || upload.httpLink() != null))
            return configuration.desireHttpMethod().orElseThrow(NullPointerException::new);

        // 3) иначе, есть аплоуды с загрузкой файла на клиенте
        if(uploads
                .stream()
                .anyMatch(upload -> upload.uploadNewFile() != null))
            return Request.HttpMethod.MULTIPART;

        // 4) если ничего не подошло, то пост
        return configuration.desireHttpMethod().orElseThrow(NullPointerException::new);
    }

    @SneakyThrows
    private Object getFieldValue(@Nonnull Object object, @Nonnull Field field) {
        field.setAccessible(true);
        return field.get(object);
    }

}
