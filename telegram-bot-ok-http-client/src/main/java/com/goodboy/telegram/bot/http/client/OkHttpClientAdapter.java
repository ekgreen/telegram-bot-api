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

package com.goodboy.telegram.bot.http.client;

import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.http.api.client.adapter.get.GetRequest;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartParameter;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartRequest;
import com.goodboy.telegram.bot.http.api.client.adapter.post.PostRequest;
import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class OkHttpClientAdapter implements HttpClientAdapter {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;

    @SneakyThrows
    public TelegramHttpResponse post(@NotNull PostRequest request) {
        final var buildRequest = new Request.Builder()
                .url(request.url())
                .post(RequestBody.create(request.payload(), JSON))
                .build();

        return handleRequest(buildRequest);
    }

    @Override
    public TelegramHttpResponse get(@NotNull GetRequest request) {
        final var urlBuilder = HttpUrl.get(request.url()).newBuilder();

        var payload = request.payload();
        if (payload != null) {
            for (var attribute : payload) {
                if (attribute.value() != null)
                    urlBuilder.addQueryParameter(attribute.key(), attribute.value());
            }
        }

        final var buildRequest = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        return handleRequest(buildRequest);
    }

    @Override
    public TelegramHttpResponse multipart(@NotNull MultipartRequest request) {
        final var multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        var payload = request.payload();
        for (var parameter : payload) {
            Object handler = parameter.value();

            if(handler instanceof MultipartParameter.SimpleMultipartParameter)
                multipartBuilder.addFormDataPart(parameter.key(), ((MultipartParameter.SimpleMultipartParameter) handler).value());
            else if(handler instanceof MultipartParameter.StreamMultipartParameter) {
                final var loader = ((MultipartParameter.StreamMultipartParameter) handler).value();
                multipartBuilder.addFormDataPart(parameter.key(), loader.fileName(), RequestBody.create(loader.bytes()));
            }
        }

        final var buildRequest = new Request.Builder()
                .url(request.url())
                .post(multipartBuilder.build())
                .build();

        return handleRequest(buildRequest);
    }

    @SneakyThrows
    private TelegramHttpResponse handleRequest(@NotNull Request request) {
        try (Response response = client.newCall(request).execute()) {
            return convert(response);
        }
    }

    @SneakyThrows
    private TelegramHttpResponse convert(@NotNull Response response) {
        return new TelegramHttpResponse()
                .setStatusCode(response.code())
                .setBody(Objects.requireNonNull(response.body()).bytes());
    }

}
