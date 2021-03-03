package com.goodboy.telegram.bot.http.api.client.adapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @param <V>
 */
public interface UniTypeRequest<V> {

    /**
     * @return url по которому требуется рпоизвести запрос
     */
    @Nonnull String url();

    /**
     * @return payload с которым требуется произвести запрос (может быть пустым, например для GET запросов)
     */
    @Nullable V payload();
}