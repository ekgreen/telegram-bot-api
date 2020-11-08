package com.goodboy.telegram.bot.spring.api.meta;

import com.goodboy.telegram.bot.spring.api.handlers.token.TokenHandler;
import com.goodboy.telegram.bot.spring.api.providers.CommandsProvider;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class BotBeanDefinition {

    /**
     * unique bot name on spring container space
     */
    private String botName;

    /**
     * The token is a string along the lines of 110201543:AAHdqTcvCH1vGWJxfSeofSAs0K5PALDsaw that is required
     * to authorize the bot and send requests to the Bot API. Keep your token secure and store it safely,
     * it can be used by anyone to control your bot.
     */
    private TokenHandler tokenHandler;

    /**
     * From 0 to N: Text of the command, 1-32 characters. Can contain only lowercase English letters, digits and underscores
     */
    private CommandsProvider commandsProvider;

    /**
     * Contains information about the current status of a webhook
     */
    private WebhookBeanDefinition webhookBeanDefinition;
}
