package com.goodboy.telegram.bot.api.method.me;

import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

import javax.annotation.Nonnull;

public interface TelegramMeApi {

    /**
     * Use this method to get info about bot
     *
     * @return on success, the sent User is returned.
     */
    @Nonnull TelegramCoreResponse<User> getMe();

}
