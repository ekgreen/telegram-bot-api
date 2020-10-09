package com.goodboy.telegram.bot.api.client;

import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

public interface TelegramHttpClientInterceptor {

    public <T,V> TelegramCoreResponse<T> intercept(Request<V> request, TelegramHttpClientInterceptorChain chain);
}
