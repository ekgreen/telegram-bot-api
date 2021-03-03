package com.goodboy.telegram.bot.api.methods.file;

import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import org.jetbrains.annotations.NotNull;

public interface TelegramFileApi {

    /**
     * Use this method to change the list of the bot's commands.
     *
     * @param api File identifier to get info about
     * @return True on success
     */
    @NotNull TelegramCoreResponse<Boolean> getFile(@NotNull GetFileApi api);

}
