package com.goodboy.telegram.bot.http.api.client.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.goodboy.telegram.bot.http.api.client.request.RequestType.COMMAND;

@RequiredArgsConstructor
public class HttpCommandBasedHandler implements HttpRequestTypeBasedHandler {

    private final ObjectMapper mapper;

    @SneakyThrows
    public <V> TelegramCoreResponse<V> handleHttpAdapterResponse(@NotNull TelegramHttpResponse response, @Nonnull Class<?> desireReturnType) {
        return mapper.readValue(response.getBody(), mapper.getTypeFactory().
                constructParametricType(TelegramCoreResponse.class, desireReturnType));
    }

    public @NotNull RequestType type() {
        return COMMAND;
    }
}
