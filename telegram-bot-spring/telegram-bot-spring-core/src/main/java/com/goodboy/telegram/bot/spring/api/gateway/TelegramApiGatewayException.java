package com.goodboy.telegram.bot.spring.api.gateway;

import com.goodboy.telegram.bot.http.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiRuntimeException;
import lombok.Getter;

@Getter
public class TelegramApiGatewayException extends TelegramApiRuntimeException {

    private final static String CODE = TelegramApiExceptionDefinitions.GATEWAY_EXCEPTION;

    private final int gatewayCode;

    public TelegramApiGatewayException(int code) {
        super(CODE);
        this.gatewayCode = code;
    }

    public TelegramApiGatewayException(int code, String message) {
        super(CODE, message);
        this.gatewayCode = code;
    }

    public TelegramApiGatewayException(int code, String message, Throwable cause) {
        super(CODE, message, cause);
        this.gatewayCode = code;
    }

    public TelegramApiGatewayException(int code, Throwable cause) {
        super(CODE, cause);
        this.gatewayCode = code;
    }

    public TelegramApiGatewayException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(CODE, message, cause, enableSuppression, writableStackTrace);
        this.gatewayCode = code;
    }
}
