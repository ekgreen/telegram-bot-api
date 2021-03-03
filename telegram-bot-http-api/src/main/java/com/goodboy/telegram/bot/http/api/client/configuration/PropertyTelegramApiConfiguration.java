package com.goodboy.telegram.bot.http.api.client.configuration;

import com.goodboy.telegram.bot.http.api.client.request.Request;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class PropertyTelegramApiConfiguration implements TelegramApiConfiguration {

    private final TelegramHttpClientProperties properties;

    @Override
    public Optional<Boolean> getThrowExceptionOnNon2XXHttpCode() {
        return Optional.of(properties.isThrowExceptionOnNonOkResponse());
    }

    @Override
    public Optional<Request.HttpMethod> desireHttpMethod() {
        return Optional.ofNullable(properties.getDesireHttpMethod());
    }
}
