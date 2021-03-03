package com.goodboy.telegram.bot.spring.impl.token;

import com.goodboy.telegram.bot.spring.api.context.TelegramApiProviderContext;
import com.goodboy.telegram.bot.spring.api.token.TelegramApiTokenProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;

public class SpringEnvironmentTelegramApiTokenProvider implements TelegramApiTokenProvider {

    public @Nullable String resolve(@Nonnull String botName, @Nonnull TelegramApiProviderContext context) {
        final var environment = context.getEnvironment();

        return environment.getProperty(format("telegram.%s.token", botName));
    }
}
