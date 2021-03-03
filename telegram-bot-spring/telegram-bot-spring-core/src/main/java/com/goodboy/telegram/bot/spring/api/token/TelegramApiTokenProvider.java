package com.goodboy.telegram.bot.spring.api.token;

import com.goodboy.telegram.bot.spring.api.context.TelegramApiProviderContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.goodboy.telegram.bot.spring.api.token.TelegramApiTokenProvider.Type.*;

public interface TelegramApiTokenProvider {

    @Nullable String resolve(@Nonnull String botName, @Nonnull TelegramApiProviderContext context);

    @Nonnull default Type type() { return STATIC; }

    enum Type{
        DYNAMIC, STATIC
    }
}
