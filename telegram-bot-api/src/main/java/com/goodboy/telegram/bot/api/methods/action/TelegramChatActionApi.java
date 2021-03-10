package com.goodboy.telegram.bot.api.methods.action;

import com.goodboy.telegram.bot.api.methods.ApiRequest;

import javax.annotation.Nonnull;

public interface TelegramChatActionApi {

    /**
     * Use this method when you need to tell the user that something is happening on the bot's side. The status is set
     * for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status).
     *
     * We only recommend using this method when a response from the bot will take a noticeable amount of time to arrive
     *
     * @param api request
     * @async
     */
    void sendChatAction(@Nonnull ApiRequest<SendChatActionApi> api);
}
