package com.goodboy.telegram.bot.http.api.client.callbacks;

import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;
import org.jetbrains.annotations.NotNull;

public interface TelegramApiCallback<T> {

    void onSuccess(@NotNull TelegramCoreResponse<T> convert);

    void onError(@NotNull Throwable exception);
}
