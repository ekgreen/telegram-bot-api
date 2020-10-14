package com.goodboy.telegram.bot.spring;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;

import javax.annotation.Nonnull;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ApplicationPropertyTokenHandler implements TokenHandler {

    private final Environment environment;

    @Override
    public String token(@Nonnull String botName) {
        return TelegramBotConfigurableDefinitions.BotDefinition.getToken(botName, environment);
    }

}
