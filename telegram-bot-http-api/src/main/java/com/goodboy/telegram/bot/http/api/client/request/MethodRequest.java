package com.goodboy.telegram.bot.http.api.client.request;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class MethodRequest<V> implements Request<V> {

    private final CallMethod callMethod;
    private V payload;
    private String token;

    public @NotNull CallMethod callMethod() {
        return callMethod;
    }

    public Optional<V> payload() {
        return Optional.ofNullable(payload);
    }

    public Optional<String> token() {
        return Optional.ofNullable(token);
    }
}
