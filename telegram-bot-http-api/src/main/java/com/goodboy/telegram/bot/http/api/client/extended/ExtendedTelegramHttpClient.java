package com.goodboy.telegram.bot.http.api.client.extended;

import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ExtendedTelegramHttpClient {

    /**
     * Extended telegram client - universal client for all request types
     *
     * @param api telegram bot api
     * @param <T> return type
     * @return telegram core response with payload
     */
    default <T> TelegramCoreResponse<T> send(@Nonnull Api api){
        return sendWithToken(api, null);
    }

    /**
     * Extended telegram client - universal client for all request types
     *
     * @param api telegram bot api
     * @param token bots token
     * @param <T> return type
     * @return telegram core response with payload
     */
    <T> TelegramCoreResponse<T> sendWithToken(@Nonnull Api api, @Nullable String token);
}
