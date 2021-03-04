package com.goodboy.telegram.bot.spring.api.data;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayRoutingResolver;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import java.util.List;

@Data
@Accessors(chain = true)
public class BotData {
    private Class<?> originBotType;
    private String beanName;

    private @Nonnull String name;
    private @Nonnull String[] paths;
    private @Nonnull TelegramApiTokenResolver telegramApiTokenResolver;

    private GatewayRoutingResolver gatewayRoutingResolver;

    private User definition;
    private List<BotCommand> commands;
}