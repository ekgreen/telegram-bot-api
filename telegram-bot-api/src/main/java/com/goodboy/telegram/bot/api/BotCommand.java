package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents a bot command
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class BotCommand {

    /**
     * Text of the command, 1-32 characters. Can contain only lowercase English letters, digits and underscores
     */
    private String command;

    /**
     * Description of the command, 3-256 characters
     */
    private String description;
}
