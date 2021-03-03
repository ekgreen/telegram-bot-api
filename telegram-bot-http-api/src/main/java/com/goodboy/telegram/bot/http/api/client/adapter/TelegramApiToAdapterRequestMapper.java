package com.goodboy.telegram.bot.http.api.client.adapter;

import com.goodboy.telegram.bot.http.api.client.request.Request;

/**
 *
 * @param <V>
 */
public interface TelegramApiToAdapterRequestMapper<V> {

    /**
     * Трансформация открытого api в формат близкий для адаптера
     *
     * @param api           запрос
     * @param <TelegramApi> тип запроса
     * @return              трансформированные данные в формат близкий для типа запроса
     */
    <TelegramApi> V transform(TelegramApi api);

    /**
     * Тип поддерживаемого запроса
     *
     * @return тип
     */
    Request.HttpMethod method();
}
