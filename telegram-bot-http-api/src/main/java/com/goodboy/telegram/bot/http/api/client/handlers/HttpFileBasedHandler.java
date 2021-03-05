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

package com.goodboy.telegram.bot.http.api.client.handlers;

import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.goodboy.telegram.bot.http.api.client.request.RequestType.COMMAND;
import static com.goodboy.telegram.bot.http.api.client.request.RequestType.FILE;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class HttpFileBasedHandler implements HttpRequestTypeBasedHandler {

    @SneakyThrows
    public <V> TelegramCoreResponse<V> handleHttpAdapterResponse(@NotNull TelegramHttpResponse response, @Nonnull Class<?> desireReturnType) {
        return new TelegramCoreResponse<V>()
                .setOk(response.getStatusCode() == 200)
                .setResult((V) response.getBody());
    }

    public @NotNull RequestType type() {
        return FILE;
    }
}
