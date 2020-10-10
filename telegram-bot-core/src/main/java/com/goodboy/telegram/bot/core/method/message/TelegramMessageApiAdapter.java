package com.goodboy.telegram.bot.core.method.message;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.method.message.SendMessageApi;
import com.goodboy.telegram.bot.api.method.message.TelegramMessageApi;
import com.goodboy.telegram.bot.api.method.message.TelegramMessageApiDefinitions;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TelegramMessageApiAdapter implements TelegramMessageApi {

    private final TelegramHttpClient client;

    public @NotNull TelegramCoreResponse<Message> sendMessage(@NotNull SendMessageApi request) {
        return client.send(new Request<>()
                .setCallName(TelegramMessageApiDefinitions.SEND_MESSAGE_CALL_METHOD)
                .setBody(request)
        );
    }
}
