package com.goodboy.telegram.bot.spring.api.environment;

import com.goodboy.telegram.bot.spring.impl.environment.TelegramAppEnvironmentDefinition.Bot;

import org.jetbrains.annotations.NotNull;

public interface TelegramEnvironment {

    /**
     * Returns root context
     * <p>
     * For example: /telegram/bot
     *
     * @return path
     */
    public @NotNull String getRootContext();

    /**
     * Method have to provide base path url for
     * telegram callbacks
     * <p>
     * For example: http://127.0.0.1:80
     *
     * @return url
     */
    public @NotNull String getBalanceProxy();

    /**
     * @param botName bot name
     * @return bot definition
     */
    public @NotNull Bot getBotByName(@NotNull String botName);
}
