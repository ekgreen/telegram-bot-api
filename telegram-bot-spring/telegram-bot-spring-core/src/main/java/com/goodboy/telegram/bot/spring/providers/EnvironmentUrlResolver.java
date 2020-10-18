package com.goodboy.telegram.bot.spring.providers;

import com.goodboy.telegram.bot.spring.environment.TelegramEnvironment;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnvironmentUrlResolver implements UrlResolver {

    private final TelegramEnvironment environment;

    public String getContextUrl(String ... paths) {
        return environment.getBalanceProxy() + environment.getRootContext() + (paths != null ? String.join("", paths) : "");
    }

    public String getContextPath(String ... paths) {
        return environment.getRootContext() + (paths != null ? String.join("", paths) : "");
    }
}
