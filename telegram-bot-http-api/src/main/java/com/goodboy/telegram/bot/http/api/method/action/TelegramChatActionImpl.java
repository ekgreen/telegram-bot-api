package com.goodboy.telegram.bot.http.api.method.action;

import com.goodboy.telegram.bot.api.methods.ApiRequest;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.api.methods.action.SendChatActionApi;
import com.goodboy.telegram.bot.api.methods.action.TelegramChatActionApi;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.callbacks.NonTelegramApiCallback;
import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TelegramChatActionImpl  implements TelegramChatActionApi {

    private final TelegramHttpClient client;

    @Override
    public void sendChatAction(@NotNull ApiRequest<SendChatActionApi> api) {
        client.sendAsync(
                new MethodRequest<>(
                        new CallMethodImpl(TelegramMethodApiDefinition.SEND_CHAT_ACTION_CALL_METHOD, RequestType.COMMAND, Boolean.class)
                )
                        .setPayload(api.getApi())
                        .setToken(api.getToken()),
                new NonTelegramApiCallback<>()
        );

    }
}
