package com.goodboy.telegram.bot.spring.api.processor.arguments;

import com.goodboy.telegram.bot.spring.api.data.BotData;

import javax.annotation.Nonnull;

public interface BotArgumentProcessor {

    void setArguments(@Nonnull BotData botData, Object[] arguments);
}
