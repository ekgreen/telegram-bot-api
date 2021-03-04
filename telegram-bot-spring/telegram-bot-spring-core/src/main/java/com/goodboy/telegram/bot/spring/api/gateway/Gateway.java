package com.goodboy.telegram.bot.spring.api.gateway;

import com.goodboy.telegram.bot.api.Update;

import javax.annotation.Nonnull;

public interface Gateway {

    Object routing(@Nonnull String botName, @Nonnull Update update);
}
