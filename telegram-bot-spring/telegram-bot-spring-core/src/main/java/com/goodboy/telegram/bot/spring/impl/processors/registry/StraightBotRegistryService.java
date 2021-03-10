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

package com.goodboy.telegram.bot.spring.impl.processors.registry;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.spring.api.context.TelegramApiProviderContext;
import com.goodboy.telegram.bot.spring.api.events.BotEventFactory;
import com.goodboy.telegram.bot.spring.api.meta.Bot;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.processor.BotRegistryService;
import com.goodboy.telegram.bot.spring.api.token.TelegramApiTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@Infrastructure
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class StraightBotRegistryService implements BotRegistryService {

    // wraps spring factories
    private final TelegramApiProviderContext providerContext;
    // event factory for throw event for over component which waiting bot data creation
    private final BotEventFactory eventFactory;
    // http client for grab data from api
    private final TelegramHttpClient client;
    // section and lock for concurrency modification def
    private final Map<String, BotData> botsByName = new HashMap<>();

    public @NotNull BotData registryBot(String beanName, Class<?> botType) {
        final boolean isBot = botType.isAnnotationPresent(Bot.class);

        if(!isBot)
            throw new IllegalArgumentException("attempt to registry no bot class = " + botType);

        final BotData botData = transformAnnotationToData(beanName, botType);

        botsByName.put(beanName, botData);

        eventFactory.createOnRegistryEvent(botData);

        return botData;
    }

    public Optional<BotData> getBotDataByBeanName(@NotNull String beanName) {
        BotData data = null;

        data = botsByName.get(beanName);

        return Optional.ofNullable(data);
    }

    public List<BotData> getBotDataByType(@NotNull Class<?> botType) {
        final List<BotData> list;

        list = botsByName.values().stream().filter(botData -> botType.isAssignableFrom(botData.getOriginBotType())).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<BotData> getAll() {
        return new ArrayList<>(botsByName.values());
    }

    private @Nonnull
    BotData transformAnnotationToData(@Nonnull String beanName, @Nonnull Class<?> botType) {
        // 0-0. Достанем аннотацию из типа
        final Bot annotation = botType.getDeclaredAnnotation(Bot.class);

        try {
            // 0. Вычислим имя бота
            final String botName = StringUtils.isNotEmpty(annotation.value()) ? annotation.value() : beanName;

            // 1. Вычислим и создадим провайдер токена для Telegram Api
            final var telegramApiTokenProvider = annotation.apiTokenProvider().getConstructor().newInstance();

            // 2.  Вычисляем токен резолвер  для Telegram Api
            final TelegramApiTokenResolver apiTokenResolver = createTelegramApiTokenResolver(telegramApiTokenProvider, botName);

            // 3. Создадим объект, отвечающий заданные о боте (те данные которые мы меожем вычислитьв  момент поднятия контекста)
            final BotData botData = new BotData(annotation.value(), annotation.path(), apiTokenResolver);

            // 4. Получим данные о боте из API Телеграм
            enrichDataByOpenApi(botData);

            // 5. Добавим вторичные поля
            botData
                    .setBeanName(beanName)
                    .setOriginBotType(botType)
                    .setGatewayRoutingResolver(annotation.apiGatewayRoutingResolver().getConstructor().newInstance())
            ;

            // 6. Залогируем
            if (log.isDebugEnabled())
                log.debug("bot data successful initialized = {}", botData);

            return botData;
        } catch (Exception transformationException) {
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "can not create bot [" + annotation.value() + "] data", transformationException);
        }
    }

    private void enrichDataByOpenApi(final BotData botData) {
        try {
            // 0. Вычислим токен с которым будем ходить в сервис
            final String token = Objects.requireNonNull(botData.getTelegramApiTokenResolver().get());

            // 1. Получим данные о командах
            client.<User, Object>send(new MethodRequest<>(
                            new CallMethodImpl(TelegramMethodApiDefinition.GET_ME, RequestType.COMMAND, User.class)
                                    .setHttpMethod(Request.HttpMethod.GET)
                    )
                            .setToken(token)
            )
                    .orThrow(NullPointerException::new)
                    .ifPresent(botData::setDefinition);

            // 2. Получим данные об операциях
            client.<BotCommand[], Object>send(new MethodRequest<>(
                            new CallMethodImpl(TelegramMethodApiDefinition.GET_MY_COMMANDS, RequestType.COMMAND, BotCommand[].class)
                                    .setHttpMethod(Request.HttpMethod.GET)
                    )
                            .setToken(token)
            )
                    .orThrow(NullPointerException::new)
                    .ifPresent(commands -> botData.setCommands(List.of(commands)));
        } catch (Exception enrichException) {
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "can not enrich bot [" + botData.getName() + "] data from open api", enrichException);
        }
    }

    private @Nonnull
    TelegramApiTokenResolver createTelegramApiTokenResolver(@Nonnull TelegramApiTokenProvider telegramApiTokenProvider, @Nonnull String botName) {
        if (telegramApiTokenProvider.type() == TelegramApiTokenProvider.Type.STATIC) {
            final String apiToken = getApiToken(telegramApiTokenProvider, botName);
            return () -> apiToken;
        }

        if (telegramApiTokenProvider.type() == TelegramApiTokenProvider.Type.DYNAMIC) {
            return () -> getApiToken(telegramApiTokenProvider, botName);
        }

        throw new UnsupportedOperationException("неизвестный тип api token провайдера = " + telegramApiTokenProvider.type());
    }

    private String getApiToken(@Nonnull TelegramApiTokenProvider telegramApiTokenProvider, @Nonnull String botName) {
        return telegramApiTokenProvider.resolve(botName, providerContext);
    }

}
