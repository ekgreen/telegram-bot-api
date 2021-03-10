package com.goodboy.telegram.bot.spring.api.events;

import org.jetbrains.annotations.NotNull;

public interface OnBotRegistry {

    void onRegistry(@NotNull BotRegisteredEvent botData);
}
