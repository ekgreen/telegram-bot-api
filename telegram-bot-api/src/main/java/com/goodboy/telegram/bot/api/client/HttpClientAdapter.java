package com.goodboy.telegram.bot.api.client;

import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public interface HttpClientAdapter {

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramCoreResponse}{@code <T>} contains the
     * response status, headers, and body ( as handled by given response body
     * handler ).
     *
     * @param <V> the request body type
     * @param request the request
     *
     * @return the response
     */
    <V> byte[] send(@Nonnull Request<V> request);

    /**
     * Sends the given request asynchronously using this client with the given
     * response body handler.
     *
     * @param <V> the request body type
     * @param request the request
     */
    <V> CompletableFuture<byte[]> sendAsync(Request<V> request);

}
