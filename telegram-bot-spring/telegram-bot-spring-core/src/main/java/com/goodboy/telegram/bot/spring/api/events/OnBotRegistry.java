package com.goodboy.telegram.bot.spring.api.events;

import com.goodboy.telegram.bot.spring.api.data.BotData;

import javax.annotation.Nonnull;

public interface OnBotRegistry {

    void onRegistry(@Nonnull BotData data);
}
