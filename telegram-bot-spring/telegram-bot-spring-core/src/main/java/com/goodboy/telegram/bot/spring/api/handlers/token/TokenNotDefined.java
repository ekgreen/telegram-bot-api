package com.goodboy.telegram.bot.spring.api.handlers.token;

import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;

public class TokenNotDefined extends TelegramApiRuntimeException {

    public TokenNotDefined(String message) {
        super(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, message);
    }
}
