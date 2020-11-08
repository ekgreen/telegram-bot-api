package com.goodboy.telegram.bot.api.method.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramCommandApiDefinitions {

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to change the list of the bot's commands
     */
    public final static String SET_MY_COMMANDS = "setMyCommands";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to get the current list of the bot's commands
     */
    public final static String GET_MY_COMMANDS = "getMyCommands";
}
