package com.goodboy.telegram.bot.spring;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;

import javax.annotation.Nonnull;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ApplicationPropertyTokenHandler implements TokenHandler {

    private final static String PATH_TEMPLATE = "telegram.api.bots.%s.token";

    private final Environment environment;

    @Override
    public String token(@Nonnull String botName) {
        return environment.getProperty(String.format(PATH_TEMPLATE, botName));
    }

}
