package com.goodboy.telegram.bot.spring.api.handlers.token;

import org.jetbrains.annotations.NotNull;

public interface Token {

    /**
     * There are two ways how define variable "token" in code:
     *
     * 1) Define bot name and autowired would be like singleton - once per app boot
     * 2) Do not define bot name and autowired would be like request scope - autowired per request
     *
     * @return token
     */
    @NotNull String value() throws TokenNotDefined;
}
