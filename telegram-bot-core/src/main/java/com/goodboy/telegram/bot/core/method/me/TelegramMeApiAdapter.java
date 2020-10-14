package com.goodboy.telegram.bot.core.method.me;

import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.method.me.TelegramMeApi;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static com.goodboy.telegram.bot.api.method.me.TelegramMeApiDefinitions.GET_ME_CALL_METHOD;

@RequiredArgsConstructor
public class TelegramMeApiAdapter implements TelegramMeApi {

    private final TelegramHttpClient client;

    public @NotNull
    TelegramCoreResponse<User> getMe() {
        return client.send(new Request<>()
                .setCallName(GET_ME_CALL_METHOD)
        );
    }
}
