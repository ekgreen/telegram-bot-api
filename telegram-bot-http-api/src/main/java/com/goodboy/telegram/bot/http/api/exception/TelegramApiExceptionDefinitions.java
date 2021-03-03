package com.goodboy.telegram.bot.http.api.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramApiExceptionDefinitions {

    /**
     * Any technical exception. Core literal (prefix) for other
     * validation exceptions
     */
    public static final String TECHNICAL_EXCEPTION = "TECHNICAL_EXCEPTION";

    /**
     * Any entity validation exception. Core literal (prefix) for other
     * validation exceptions
     */
    public static final String VALIDATION_EXCEPTION = "VALIDATION_EXCEPTION";

    /**
     * Http response returned with status code different from 200
     */
    public static final String NOT_200_HTTP_STATUS_CODE = "VALIDATION_EXCEPTION:HTTP:NOT_200";

    /**
     * Any http response exception. Core literal (prefix) for other
     * http integration exceptions
     */
    public static final String HTTP_RESPONSE_ERROR = "HTTP_RESPONSE_ERROR";

    /**
     * Any http request-creation exception. Core literal (prefix) for other
     * http integration exceptions
     */
    public static final String HTTP_REQUEST_ERROR = "HTTP_REQUEST_ERROR";
}
