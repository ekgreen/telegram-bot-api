package com.goodboy.telegram.bot.core.method.webhook;

import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.method.webhook.*;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class UnauthorizedTelegramWebhookApiAdapter implements UnauthorizedTelegramWebhookApi {

    private final TelegramHttpClient client;

    public @NotNull
    TelegramCoreResponse<Boolean> setWebhook(@NotNull String token, @NotNull SetWebhookApi request) {
        return client.send(new Request<>()
                .setToken(token)
                .setResponseType(Boolean.class)
                .setCallName(TelegramWebhookApiDefinitions.SET_WEBHOOK_CALL_METHOD)
                .setBody(request)
        );
    }


    public @NotNull
    TelegramCoreResponse<Boolean> deleteWebhook(@NotNull String token) {
        return client.send(new Request<>()
                .setToken(token)
                .setResponseType(Boolean.class)
                .setCallName(TelegramWebhookApiDefinitions.DELETE_WEBHOOK_CALL_METHOD)
        );
    }

    public @NotNull
    TelegramCoreResponse<WebhookInfo> getWebhookInfo(@NotNull String token) {
        return client.send(new Request<>()
                .setToken(token)
                .setResponseType(WebhookInfo.class)
                .setCallName(TelegramWebhookApiDefinitions.GET_WEBHOOK_INFO_CALL_METHOD)
        );
    }

}
