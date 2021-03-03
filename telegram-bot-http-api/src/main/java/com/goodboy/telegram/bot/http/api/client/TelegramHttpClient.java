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
