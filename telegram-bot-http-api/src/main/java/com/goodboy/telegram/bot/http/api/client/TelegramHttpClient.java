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

package com.goodboy.telegram.bot.http.api.client;

import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Универсальный клиент для отправки клиентских запросов к/используя API Телеграм
 *
 * Все клиентские запросы в библиотеке производется при помощи данного интерфейса. Он имеет
 * два типовых метода для отправки запросов синхронный и асинхронный вызов. Результатом работы
 * методов будет являться стандартный ответ {@link TelegramCoreResponse} в формате API Телеграм (то есть ответ никак не
 * видоизменяется интерфейсом). Единственное, что для получения асинхронного ответа его
 * обарачивают во future.
 *
 * Базовой имплементацие интерфейса является класс {@link BaseTelegramHttpClient}
 *
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public interface TelegramHttpClient {

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramCoreResponse}{@code <T>} contains the
     * response status, headers, and body ( as handled by given response body
     * handler ).
     *
     * @param <V> the request body type
     * @param <T> the response body type
     * @param request the request
     *
     * @return the response
     */
    <T, V> TelegramCoreResponse<T> send(@NotNull Request<V> request);

    /**
     * Sends the given request asynchronously using this client with the given
     * response body handler.
     *
     * @param <V> the request body type
     * @param <T> the response body type
     * @param request the request
     */
    <T, V> CompletableFuture<TelegramCoreResponse<T>> sendAsync(Request<V> request);
}
