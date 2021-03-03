package com.goodboy.telegram.bot.http.api.client.context;

import com.goodboy.telegram.bot.api.BotCommand;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TelegramApiUpdateContextImpl implements UpdateContext {
    private final Integer botId;
    private final String botName;
    private final String botToken;
    private List<BotCommand> botCommands;
}
