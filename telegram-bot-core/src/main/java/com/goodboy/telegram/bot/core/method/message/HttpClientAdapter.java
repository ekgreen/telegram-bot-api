package com.goodboy.telegram.bot.core.method.message;

import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

import java.util.concurrent.CompletableFuture;

public interface HttpClientAdapter {

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramCoreResponse}{@code <T>} contains the
     * response status, headers, and body ( as handled by given response body
     * handler ).
     *
     * @param <R> the request body type
     * @param <T> the response body type
     * @param request the request
     *
     * @return the response
     */
    <T, R> TelegramCoreResponse<T> send(R request);

    /**
     * Sends the given request asynchronously using this client with the given
     * response body handler.
     *
     * @param <R> the request body type
     * @param <T> the response body type
     * @param request the request
     */
    <T, R> CompletableFuture<TelegramCoreResponse<T>> sendAsync(R request);

    interface Builder{


    }
}
