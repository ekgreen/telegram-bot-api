package com.goodboy.telegram.bot.core.method.webhook;

import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.method.webhook.SetWebhookApi;
import com.goodboy.telegram.bot.api.method.webhook.TelegramWebhookApi;
import com.goodboy.telegram.bot.api.method.webhook.TelegramWebhookApiDefinitions;
import com.goodboy.telegram.bot.api.method.webhook.WebhookInfo;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TelegramWebhookApiAdapter implements TelegramWebhookApi {

    private final TelegramHttpClient client;

    public @NotNull TelegramCoreResponse<Boolean> setWebhook(@NotNull SetWebhookApi request) {
        return client.send(new Request<>()
                .setCallName(TelegramWebhookApiDefinitions.SET_WEBHOOK_CALL_METHOD)
                .setResponseType(Boolean.class)
                .setBody(request)
        );
    }

    public @NotNull TelegramCoreResponse<Boolean> deleteWebhook() {
        return client.send(new Request<>()
                .setCallName(TelegramWebhookApiDefinitions.DELETE_WEBHOOK_CALL_METHOD)
                .setResponseType(Boolean.class)
        );
    }

    public @NotNull TelegramCoreResponse<WebhookInfo> getWebhookInfo() {
        return client.send(new Request<>()
                .setCallName(TelegramWebhookApiDefinitions.GET_WEBHOOK_INFO_CALL_METHOD)
                .setResponseType(WebhookInfo.class)
        );
    }

}
