package com.goodboy.telegram.bot.api.methods;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;

@Data
@Accessors(chain = true)
public class ApiRequest<T extends Api> {

    private final static ApiRequest<? extends Api> EMPTY = new ApiRequest<>();

    // request payload
    private @Nullable T api;
    // specific bot token
    private @Nullable String token;

    @SuppressWarnings("unchecked")
    public static <T extends Api> ApiRequest<T> empty() {
        return (ApiRequest<T>) EMPTY;
    }
}
