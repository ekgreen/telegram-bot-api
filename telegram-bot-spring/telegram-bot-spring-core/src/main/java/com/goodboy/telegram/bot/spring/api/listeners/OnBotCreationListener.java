package com.goodboy.telegram.bot.spring.api.listeners;

import com.goodboy.telegram.bot.spring.api.bot.Bot;
import com.goodboy.telegram.bot.spring.api.meta.WebhookApi;
import com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition;
import org.jetbrains.annotations.NotNull;

public interface OnBotCreationListener {

    /**
     * Callback after webhook bean creation
     *
     * @param definition metadata describing webhook + link to instance
     *
     * @see Bot
     * @see WebhookApi
     * @see BotBeanDefinition
     */
    public void onBotCreation(@NotNull Bot bot, @NotNull BotBeanDefinition definition);
}
