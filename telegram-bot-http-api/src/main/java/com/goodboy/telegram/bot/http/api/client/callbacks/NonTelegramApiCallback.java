package com.goodboy.telegram.bot.http.api.client.callbacks;

import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class NonTelegramApiCallback<T> implements TelegramApiCallback<T>{
    @Override
    public void onSuccess(@NotNull TelegramCoreResponse<T> convert) {}

    @Override
    public void onError(@NotNull Throwable exception) {}
}
