package com.goodboy.telegram.bot.http.api.method.me;

import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.api.methods.me.TelegramMeApi;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TelegramMeImpl implements TelegramMeApi {

    private final TelegramHttpClient client;

    @Override
    public @NotNull TelegramCoreResponse<User> getMe() {
        return client.send(new MethodRequest<>(
                        new CallMethodImpl(TelegramMethodApiDefinition.GET_ME, RequestType.COMMAND, User.class)
                                .setHttpMethod(Request.HttpMethod.GET)
                )
        );
    }
}
