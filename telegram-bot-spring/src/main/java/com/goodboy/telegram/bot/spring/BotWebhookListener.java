package com.goodboy.telegram.bot.spring;

import javax.annotation.Nonnull;

public interface BotWebhookListener {

    /**
     * Callback after webhook bean creation
     *
     * @param info metadata describing webhook + link to instance
     *
     * @see Webhook
     * @see WebhookApi
     * @see WebhookBeanDefinition
     */
    public void onWebhookCreation(@Nonnull WebhookBeanDefinition info);
}
