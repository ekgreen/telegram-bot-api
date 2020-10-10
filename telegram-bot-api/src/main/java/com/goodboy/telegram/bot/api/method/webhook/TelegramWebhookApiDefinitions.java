package com.goodboy.telegram.bot.api.method.webhook;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramWebhookApiDefinitions {

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to specify a url and receive incoming updates via an outgoing webhook.
     */
    public final static String SET_WEBHOOK_CALL_METHOD = "setWebhook";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to remove webhook integration if you decide to switch back to getUpdates.
     */
    public final static String DELETE_WEBHOOK_CALL_METHOD = "deleteWebhook";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to get current webhook status. Requires no parameters.
     */
    public final static String GET_WEBHOOK_INFO_CALL_METHOD = "getWebhookInfo";
}
