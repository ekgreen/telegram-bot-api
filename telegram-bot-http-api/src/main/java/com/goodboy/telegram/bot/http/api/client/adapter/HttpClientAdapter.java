/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.http.api.client.adapter;

import com.goodboy.telegram.bot.http.api.client.adapter.get.GetRequest;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartRequest;
import com.goodboy.telegram.bot.http.api.client.adapter.post.PostRequest;
import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;

import javax.annotation.Nonnull;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
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
