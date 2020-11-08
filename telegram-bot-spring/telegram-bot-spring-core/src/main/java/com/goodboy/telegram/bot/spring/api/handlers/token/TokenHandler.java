package com.goodboy.telegram.bot.spring.api.handlers.token;

import org.jetbrains.annotations.NotNull;

public interface TokenHandler {

    String token(@NotNull String botName);
}
