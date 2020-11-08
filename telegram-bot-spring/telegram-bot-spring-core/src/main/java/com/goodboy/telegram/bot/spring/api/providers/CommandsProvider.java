package com.goodboy.telegram.bot.spring.api.providers;

import org.jetbrains.annotations.NotNull;

public interface CommandsProvider {

    @NotNull String[] commands(@NotNull String botName);
}
