package com.goodboy.telegram.bot.core.method.webhook;

import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.method.token.TokenSupplier;
import com.goodboy.telegram.bot.api.method.webhook.*;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.goodboy.telegram.bot.api.method.webhook.TelegramWebhookApiDefinitions.*;

@AllArgsConstructor
@RequiredArgsConstructor
public class TelegramWebhookApiAdapter implements TelegramWebhookApi {

    private final TelegramHttpClient client;
    private TokenSupplier tokenizer = () -> null;

    public @NotNull TelegramCoreResponse<Boolean> setWebhook(@NotNull SetWebhookApi api) {
        return setWebhook((request) -> request
                .setToken(tokenizer.get())
                .setBody(api)
        );
    }

    public @NotNull TelegramCoreResponse<Boolean> deleteWebhook() {
        return deleteWebhook((request) -> request
                .setToken(tokenizer.get())
        );
    }

    public @NotNull TelegramCoreResponse<WebhookInfo> getWebhookInfo() {
        return getWebhookInfo((request) -> request
                .setToken(tokenizer.get())
        );
    }

    @Override
    public @NotNull TelegramCoreResponse<Boolean> setWebhook(Consumer<Request<SetWebhookApi>> handler) {
        return handleTelegramRequest(SET_WEBHOOK_CALL_METHOD, Boolean.class, handler);
    }

    @Override
    public @NotNull TelegramCoreResponse<Boolean> deleteWebhook(Consumer<Request<DeleteWebhookApi>> handler) {
        return handleTelegramRequest(DELETE_WEBHOOK_CALL_METHOD, Boolean.class, handler);
    }

    @Override
    public @NotNull TelegramCoreResponse<WebhookInfo> getWebhookInfo(Consumer<Request<GetWebhookApi>> handler) {
        return handleTelegramRequest(GET_WEBHOOK_INFO_CALL_METHOD, WebhookInfo.class, handler);
    }

    private <T,R> TelegramCoreResponse<R> handleTelegramRequest(@NotNull String callName, Class<?> type, @NotNull Consumer<Request<T>> handler) {
        final Request<T> request = new Request<T>()
                .setCallName(callName)
                .setResponseType(type);

        handler.accept(request);

        return client.send(request);
    }
}
