package com.goodboy.telegram.bot.api.methods.me;

import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import org.jetbrains.annotations.NotNull;


public interface TelegramMeApi {

    /**
     * Use this method to get info about bot
     *
     * @return on success, the sent User is returned.
     */
    @NotNull TelegramCoreResponse<User> getMe();

}
