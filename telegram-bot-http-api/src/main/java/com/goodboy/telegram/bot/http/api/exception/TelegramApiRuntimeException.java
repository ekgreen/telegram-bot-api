package com.goodboy.telegram.bot.http.api.exception;

import lombok.Getter;

@Getter
public class TelegramApiRuntimeException extends RuntimeException {

    private final String code;

    public TelegramApiRuntimeException() {
        this(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION);
    }

    public TelegramApiRuntimeException(String code) {
        super();
        this.code = code;
    }

    public TelegramApiRuntimeException(String code, String message) {
        super(message);
        this.code = code;
    }

    public TelegramApiRuntimeException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public TelegramApiRuntimeException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    protected TelegramApiRuntimeException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
