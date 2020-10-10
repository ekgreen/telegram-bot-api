package com.goodboy.telegram.bot.api.method.message;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramMessageApiDefinitions {

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to send text messages
     */
    public final static String SEND_MESSAGE_CALL_METHOD = "sendMessage";

}
