package com.goodboy.telegram.bot.http.api.client.handlers;

import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;

import javax.annotation.Nonnull;


/**
 * Трансформатор (обработчик) набора байт {@link TelegramHttpResponse#getBody()} в реальный ответ от API Телеграм
 *
 * Зачем он нужен, если ответ универсальный? - универсальный да несовсем, например при получении файла и при получении
 * сообщения с сервера, форматы не унифицированы. Таким образом данный интерфейс служит трансформатором набора байт
 * в определенный формат метода, исходя из оперделенный метаданных запроса или ответа.
 *
 * Также интерфейс подразумевает выборку трансформатора исходя из типа запроса
 *
 * @see TelegramCoreResponse
 * @see TelegramHttpResponse
 * @see RequestType
 */
public interface HttpRequestTypeBasedHandler {

    /**
     * Метод трансформации байт в универсальный формат ответа
     *
     * @param response          обертка над набором байт
     * @param desireReturnType  класс сообщения (внутри универсального ответа {@link TelegramCoreResponse#getResult()}
     * @param <V>               тип сообщения
     * @return                  ответ в универсальном формате
     */
    <V> TelegramCoreResponse<V> handleHttpAdapterResponse(@Nonnull TelegramHttpResponse response, @Nonnull Class<?> desireReturnType);

    /**
     * @return тип запроса, поддерживаемый конкретной имплементацией трансформатора
     */
    @Nonnull RequestType type();
}
