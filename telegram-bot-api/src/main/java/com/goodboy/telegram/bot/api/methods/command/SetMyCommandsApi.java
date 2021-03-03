package com.goodboy.telegram.bot.api.methods.command;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.methods.Api;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@TelegramApi
@Accessors(chain = true)
public class SetMyCommandsApi implements Api {

    /**
     * A JSON-serialized list of bot commands to be set as the list of the bot's commands.
     * At most 100 commands can be specified
     */
    private List<BotCommand> commands;
}
