package com.goodboy.telegram.bot.http.api.client.request;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class CallMethodImpl implements Request.CallMethod {

    private final String name;
    private final RequestType requestType;
    private final Class<?> returnType;
    private Request.HttpMethod httpMethod;

    public @NotNull String name() {
        return name;
    }

    public @NotNull RequestType type() {
        return requestType;
    }

    public @NotNull Class<?> desireReturnType() {
        return returnType;
    }

    public Optional<Request.HttpMethod> desireHttpMethod() {
        return Optional.ofNullable(httpMethod);
    }
}
