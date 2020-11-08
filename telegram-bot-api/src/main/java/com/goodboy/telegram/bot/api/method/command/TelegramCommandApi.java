package com.goodboy.telegram.bot.api.method.command;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface TelegramCommandApi {

    /**
     * Use this method to change the list of the bot's commands.
     *
     * @param commands A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most
     *                 100 commands can be specified
     * @return True on success
     */
    @NotNull TelegramCoreResponse<Boolean> setMyCommands(@NotNull SetMyCommandsApi commands);

    default @NotNull TelegramCoreResponse<Boolean> setMyCommands(Consumer<Request<SetMyCommandsApi>> handler){
        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "missed token");
    }

    /**
     * Use this method to get the current list of the bot's commands. Requires no parameters.
     *
     * @return array of BotCommand on success.
     */
    @NotNull TelegramCoreResponse<BotCommand[]> getMyCommands();
}
