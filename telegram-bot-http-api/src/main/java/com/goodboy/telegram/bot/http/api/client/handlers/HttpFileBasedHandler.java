package com.goodboy.telegram.bot.http.api.client.handlers;

import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.goodboy.telegram.bot.http.api.client.request.RequestType.COMMAND;

@RequiredArgsConstructor
public class HttpFileBasedHandler implements HttpRequestTypeBasedHandler {

    @SneakyThrows
    public <V> TelegramCoreResponse<V> handleHttpAdapterResponse(@NotNull TelegramHttpResponse response, @Nonnull Class<?> desireReturnType) {
        return new TelegramCoreResponse<V>()
                .setOk(response.getStatusCode() == 200)
                .setResult((V) response.getBody());
    }

    public @NotNull RequestType type() {
        return COMMAND;
    }
}
