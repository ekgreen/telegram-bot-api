package com.goodboy.telegram.bot.spring.impl.sender;

import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.api.methods.message.SendStickerApi;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.spring.api.sender.ApiMethodExecutor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class StickerApiSenderExecutor implements ApiMethodExecutor<SendStickerApi> {

    private final TelegramHttpClient client;

    @Override
    public void handle(@NotNull UpdateContext context, @NotNull SendStickerApi api) {
        client.send(new MethodRequest<>(
                        new CallMethodImpl(TelegramMethodApiDefinition.SEND_STICKER_CALL_METHOD, RequestType.COMMAND, User.class)
                                .setHttpMethod(Request.HttpMethod.POST)
                )
                .setPayload(api)
                .setToken(context.getBotToken())
        );
    }

    public @NotNull Class<SendStickerApi> type() {
        return SendStickerApi.class;
    }
}
