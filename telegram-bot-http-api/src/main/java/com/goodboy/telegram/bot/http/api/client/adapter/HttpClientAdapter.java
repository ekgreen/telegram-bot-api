package com.goodboy.telegram.bot.http.api.client.adapter;

import com.goodboy.telegram.bot.http.api.client.adapter.get.GetRequest;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartRequest;
import com.goodboy.telegram.bot.http.api.client.adapter.post.PostRequest;
import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;

import javax.annotation.Nonnull;

/**
 *
 */
public interface HttpClientAdapter {

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramHttpResponse}contains the
     * response status and body
     *
     * Use method POST to send body
     *
     * @param request the request
     *
     * @return the response
     */
    TelegramHttpResponse post(@Nonnull PostRequest request);

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramHttpResponse}contains the
     * response status and body
     *
     * Use method GET without body
     *
     * @return the response
     */
    TelegramHttpResponse get(@Nonnull GetRequest request);

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramHttpResponse}contains the
     * response status and body
     *
     * Use method POST to send multipart body
     *
     * @return the response
     */
    TelegramHttpResponse multipart(@Nonnull MultipartRequest request);



}
