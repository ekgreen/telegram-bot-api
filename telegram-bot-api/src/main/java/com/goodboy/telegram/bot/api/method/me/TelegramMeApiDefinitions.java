package com.goodboy.telegram.bot.api.method.me;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramMeApiDefinitions {

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to specify a url and get info about chat
     */
    public final static String GET_ME_CALL_METHOD = "getMe";
}
