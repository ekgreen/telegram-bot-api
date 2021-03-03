package com.goodboy.telegram.bot.http.api.client.adapter.get;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public interface QueryAttribute {

    /**
     * Разделить ключа и значения
     */
    public static final String QUERY_ATTRIBUTE_DELIMITER = "=";

    /**
     * @return ключ параметра запроса
     */
    @Nonnull String key();

    /**
     * @return значение параметра запроса
     */
    @Nullable String value();

    /**
     * @return готовый атрибут для подстановки в запрос
     */
    default @Nonnull String attribute(){
        return key() + QUERY_ATTRIBUTE_DELIMITER + value();
    }
}
