package com.goodboy.telegram.bot.api.client.adapter;

import com.goodboy.telegram.bot.api.response.TelegramHttpResponse;

import java.util.concurrent.CompletableFuture;

public interface HttpClientAdapter {

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramHttpResponse}contains the
     * response status and body
     *
     * Use method POST to send body
     *
     * @param <V> the request body type
     * @param request the request
     *
     * @return the response
     */
    <V> TelegramHttpResponse post(UniTypeRequest<V> request);

    /**
     * Sends the given request asynchronously
     *
     * @param <V> the request body type
     * @param request the request
     *
     * @see HttpClientAdapter#post(UniTypeRequest)
     */
    <V> CompletableFuture<TelegramHttpResponse> postAsync(UniTypeRequest<V> request);

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramHttpResponse}contains the
     * response status and body
     *
     * Use method GET without body
     *
     * @return the response
     */
    TelegramHttpResponse get(UniTypeRequest<?> request);

    /**
     * Sends the given request asynchronously
     *
     * @param request the request
     *
     * @see HttpClientAdapter#get(UniTypeRequest)
     */
    CompletableFuture<TelegramHttpResponse> getAsync(UniTypeRequest<?> request);

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramHttpResponse}contains the
     * response status and body
     *
     * Use method POST to send multipart body
     *
     * @return the response
     */
    <V> TelegramHttpResponse multipart(UniTypeRequest<V> request);

    /**
     * Sends the given request asynchronously
     *
     * @param request the request
     *
     * @see HttpClientAdapter#multipart(UniTypeRequest)
     */
    <V> CompletableFuture<TelegramHttpResponse> multipartAsync(UniTypeRequest<V> request);

}
