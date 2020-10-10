package com.goodboy.telegram.bot.spring;

import javax.annotation.Nonnull;

public interface TokenHandler {

    String token(@Nonnull String botName);
}
