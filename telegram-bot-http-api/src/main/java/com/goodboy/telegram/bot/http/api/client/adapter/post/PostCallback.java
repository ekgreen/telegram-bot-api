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

package com.goodboy.telegram.bot.http.api.client.adapter.post;

import com.goodboy.telegram.bot.http.api.client.adapter.Call;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapterCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.TelegramApiToAdapterRequestMapper;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartRequest;
import com.goodboy.telegram.bot.http.api.client.callbacks.Callback;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod;
import static com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod.POST;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class PostCallback implements HttpClientAdapterCallback {

    private final TelegramApiToAdapterRequestMapper<byte[]> mapper;

    @Override
    public <V> Call callback(@NotNull HttpClientAdapter adapter, @Nonnull String url, @Nullable V payload) {
        return () -> adapter.post(createPostRequest(url,payload));
    }

    @Override
    public <V> void callback(@NotNull HttpClientAdapter adapter, @NotNull String url, @Nullable V payload, @NotNull Callback callback) {
        adapter.postAsync(createPostRequest(url,payload), callback);
    }

    private <V> @NotNull PostRequest createPostRequest(@Nonnull String url, @Nullable V payload) {
        return new CallablePostRequest(url, mapper.transform(payload));
    }

    @Override
    public HttpMethod method() {
        return POST;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    private static class CallablePostRequest implements PostRequest{
        private final String url;
        private final byte[] payload;
    }
}
