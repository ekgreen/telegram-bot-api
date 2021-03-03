package com.goodboy.telegram.bot.http.api.client.adapter;

import com.goodboy.telegram.bot.http.api.client.request.Request;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public interface HttpClientAdapterCallback {

    /**
     * Возвращает функцию для запроса, использую http-adapter
     *
     * @param adapter адаптер над одним из http-клиентов
     * @param url     куда отправлять запрос
     * @param payload то что требуется отправить
     * @param <V>     тип отправляемых данных
     * @return        функция
     */
    <V> Callback callback(@Nonnull HttpClientAdapter adapter, @Nonnull String url, @Nullable V payload);

    /**
     * Тип поддерживаемого запроса
     *
     * @return тип
     */
    Request.HttpMethod method();
}
