package com.goodboy.telegram.bot.spring.api.processor;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import lombok.Data;

import java.util.List;

@Data
public class BotData {
    private final String name;
    private final String[] paths;
    private final TelegramApiTokenResolver telegramApiTokenResolver;

    private User definition;
    private List<BotCommand> commands;
}