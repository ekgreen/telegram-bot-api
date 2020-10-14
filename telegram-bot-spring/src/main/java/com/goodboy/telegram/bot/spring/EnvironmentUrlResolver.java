package com.goodboy.telegram.bot.spring;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnvironmentUrlResolver implements UrlResolver {

    private final TelegramEnvironment environment;

    public String getContextUrl() {
        return environment.getBalanceProxy() + environment.getRootContext();
    }
}
