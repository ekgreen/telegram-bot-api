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

import com.goodboy.telegram.bot.http.api.client.callbacks.Callback;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public interface HttpClientAdapterCallback {

    /**
     * Возвращает функцию для запроса, использую http-adapter
     *
     * @param adapter адаптер над одним из http-клиентов
     * @param url     куда отправлять запрос
     * @param payload то что требуется отправить
     * @param <V>     тип отправляемых данных
     * @return        функция
     */
    <V> Call callback(@Nonnull HttpClientAdapter adapter, @Nonnull String url, @Nullable V payload);

    /**
     * Использует функцию для обработки ответа, используя http-adapter
     *
     * @param adapter   адаптер над одним из http-клиентов
     * @param url       куда отправлять запрос
     * @param payload   то что требуется отправить
     * @param callback  функция для обработки ответа
     * @param <V>       тип отправляемых данных
     */
    <V> void callback(@Nonnull HttpClientAdapter adapter, @Nonnull String url, @Nullable V payload , @NotNull Callback callback);


    /**
     * Тип поддерживаемого запроса
     *
     * @return тип
     */
    Request.HttpMethod method();
}
