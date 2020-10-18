package com.goodboy.telegram.bot.spring.providers;

import javax.annotation.Nonnull;

public interface TokenHandler {

    String token(@Nonnull String botName);
}
