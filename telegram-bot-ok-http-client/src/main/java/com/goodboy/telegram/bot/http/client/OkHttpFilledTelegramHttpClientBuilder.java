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

import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.builder.FilledTelegramHttpClientBuilder;
import com.google.common.base.Suppliers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public class OkHttpFilledTelegramHttpClientBuilder extends FilledTelegramHttpClientBuilder {

    // default ok http client - single instance on all calls
    private final static Supplier<OkHttpClient> OK_HTTP_CLIENT_FACTORY = Suppliers.memoize(OkHttpFilledTelegramHttpClientBuilder::buildOkHttpClient);

    private OkHttpClient client;

    public OkHttpFilledTelegramHttpClientBuilder client(@NotNull OkHttpClient client) {
        Objects.requireNonNull(client, "client cannot be null");
        this.client = client;
        return this;
    }

    public TelegramHttpClient build() {
        return build(new OkHttpClientAdapter(
                buildClient()
        ));
    }

    private @Nonnull
    OkHttpClient buildClient() {
        return client != null ? client : OK_HTTP_CLIENT_FACTORY.get();
    }

    private static OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .build();
    }

    private static Interceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
