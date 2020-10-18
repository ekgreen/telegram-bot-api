package com.goodboy.telegram.bot.core.client;

import com.goodboy.telegram.bot.core.client.uni.UniTypeRequest;

import java.util.concurrent.CompletableFuture;

public interface UniTypedHttpClientAdapter {

    <V> TelegramHttpResponse post(UniTypeRequest<V> request);

    <V> CompletableFuture<TelegramHttpResponse> postAsync(UniTypeRequest<V> request);

    TelegramHttpResponse get(UniTypeRequest<?> request);

    CompletableFuture<TelegramHttpResponse> getAsync(UniTypeRequest<?> request);

    <V> TelegramHttpResponse multipart(UniTypeRequest<V> request);

    <V> CompletableFuture<TelegramHttpResponse> multipartAsync(UniTypeRequest<V> request);
}
