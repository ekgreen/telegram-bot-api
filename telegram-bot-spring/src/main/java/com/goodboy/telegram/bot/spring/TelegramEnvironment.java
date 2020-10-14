package com.goodboy.telegram.bot.spring;

import javax.annotation.Nonnull;

public interface TelegramEnvironment {

    /**
     * Returns root context
     *
     * For example: /telegram/bot
     *
     * @return path
     */
    public @Nonnull String getRootContext();

    /**
     * Method have to provide base path url for
     * telegram callbacks
     *
     * For example: http://127.0.0.1:80
     *
     * @return url
     */
    public @Nonnull String getBalanceProxy();

    /**
     * Method return bots webhook definition
     *
     * @param botName bot's name
     * @return definition
     */
    public @Nonnull WebhookBeanDefinition getWebhookDefinition(@Nonnull String botName);
}
