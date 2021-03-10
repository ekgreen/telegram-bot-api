package com.goodboy.telegram.bot.http.api.client.callbacks;

import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;
import org.jetbrains.annotations.NotNull;

public interface Callback {

    void onSuccess(@NotNull TelegramHttpResponse response);

    void onError(@NotNull Throwable exception);
}
