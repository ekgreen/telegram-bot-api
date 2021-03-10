package com.goodboy.telegram.bot.spring.api.actions;

import com.goodboy.telegram.bot.api.methods.action.Action;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiRuntimeException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class FutureFastAction {

    // fast action for chat
    private final String chatId;

    // fast action from bot with token
    private final TelegramApiTokenResolver token;

    // something that have to be executing
    private final Future<?> executing;

    //something that produces action/s
    private final Supplier<Action> actionProducer;

    // who to know that 'some' is already executed? - call future method
    // volatile read/write is ok
    public boolean isDone() {
        return executing.isDone();
    }

    // return chat id
    public @NotNull String getChatId() {
        return chatId;
    }

    // return bot token
    public @NotNull String getToke() {
        return token.get();
    }

    public @NotNull
    Action next() throws TelegramApiRuntimeException {
        if (isDone())
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "heavy weight process already done");

        return actionProducer.get();
    }
}
