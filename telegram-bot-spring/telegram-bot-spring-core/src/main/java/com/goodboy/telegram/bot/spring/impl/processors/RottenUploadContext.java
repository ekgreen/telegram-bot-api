package com.goodboy.telegram.bot.spring.impl.processors;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiRuntimeException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class RottenUploadContext implements UpdateContext {

    public final static UpdateContext INSTANCE = new RottenUploadContext();

    public @NotNull Integer getBotId() {
        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "rotten context");
    }

    public @NotNull String getBotName() {
        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "rotten context");
    }

    public @NotNull String getBotToken() {
        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "rotten context");
    }

    public @NotNull List<BotCommand> getBotCommands() {
        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "rotten context");
    }
}
