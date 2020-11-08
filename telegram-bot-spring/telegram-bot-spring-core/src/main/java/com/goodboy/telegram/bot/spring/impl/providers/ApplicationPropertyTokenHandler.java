package com.goodboy.telegram.bot.spring.impl.providers;

import com.goodboy.telegram.bot.spring.api.environment.TelegramEnvironment;
import com.goodboy.telegram.bot.spring.api.handlers.token.TokenHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ApplicationPropertyTokenHandler implements TokenHandler {

    private final TelegramEnvironment environment;

    @Override
    public String token(@NotNull String botName) {
        return environment.getBotByName(botName).getToken();
    }

}
