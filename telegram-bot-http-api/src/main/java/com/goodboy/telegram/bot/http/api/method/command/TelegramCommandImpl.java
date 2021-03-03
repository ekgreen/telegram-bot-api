package com.goodboy.telegram.bot.http.api.method.command;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.api.methods.command.SetMyCommandsApi;
import com.goodboy.telegram.bot.api.methods.command.TelegramCommandApi;
import com.goodboy.telegram.bot.api.methods.me.TelegramMeApi;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class TelegramCommandImpl implements TelegramCommandApi {

    private final TelegramHttpClient client;

    @Override
    public @NotNull TelegramCoreResponse<Boolean> setMyCommands(@NotNull SetMyCommandsApi commands) {
        return null;
    }

    @Override
    public @NotNull TelegramCoreResponse<BotCommand[]> getMyCommands() {
        return client.send(new MethodRequest<>(
                        new CallMethodImpl(TelegramMethodApiDefinition.GET_MY_COMMANDS, RequestType.COMMAND, BotCommand[].class)
                                .setHttpMethod(Request.HttpMethod.GET)
                )
        );
    }
}
