package com.goodboy.telegram.bot.core.method.command;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.method.command.SetMyCommandsApi;
import com.goodboy.telegram.bot.api.method.command.TelegramCommandApi;
import com.goodboy.telegram.bot.api.method.command.TelegramCommandApiDefinitions;
import com.goodboy.telegram.bot.api.method.token.TokenSupplier;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@AllArgsConstructor
@RequiredArgsConstructor
public class TelegramCommandApiAdapter implements TelegramCommandApi {

    private final TelegramHttpClient client;
    private TokenSupplier tokenizer = () -> null;

    @Override
    public @NotNull TelegramCoreResponse<Boolean> setMyCommands(@NotNull SetMyCommandsApi commands) {
        return setMyCommands(request -> request.setBody(commands));
    }

    @Override
    public @NotNull TelegramCoreResponse<Boolean> setMyCommands(Consumer<Request<SetMyCommandsApi>> handler) {
        final Request<SetMyCommandsApi> request = new Request<SetMyCommandsApi>()
                .setCallName(TelegramCommandApiDefinitions.SET_MY_COMMANDS)
                .setToken(tokenizer.get())
                .setResponseType(Boolean.class);

        handler.accept(request);
        return client.send(request);
    }

    @Override
    public @NotNull TelegramCoreResponse<BotCommand[]> getMyCommands() {
        return client.send(new Request<>()
                .setCallName(TelegramCommandApiDefinitions.SET_MY_COMMANDS)
                .setToken(tokenizer.get())
                .setResponseType(Boolean.class)
        );
    }
}
