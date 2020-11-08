package com.goodboy.telegram.bot.spring.api.meta;

import com.goodboy.telegram.bot.spring.api.handlers.token.TokenHandler;
import com.goodboy.telegram.bot.spring.impl.providers.ApplicationPropertyTokenHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bot {

    /**
     * Unique bot name on spring container space
     *
     * @return bot name
     */
    @AliasFor("value")
    String name() default "";

    /**
     * @return bot name
     */
    @AliasFor("name")
    String value() default "";

    /**
     *  This method to specify a url and receive incoming updates via an outgoing webhook.
     *  Whenever there is an update for the bot, we will send an HTTPS POST request to the specified url,
     *  containing a JSON-serialized Update. In case of an unsuccessful request, we will give up after
     *  a reasonable amount of attempts
     *
     * @return hook
     */
    WebhookApi hook();

    /**
     * Use this method to change the list of the bot's commands.
     *
     * @return commands
     */
    BotCommand[] commands() default {};

    /**
     * The token is a string along the lines of 110201543:AAHdqTcvCH1vGWJxfSeofSAs0K5PALDsaw that is required
     * to authorize the bot and send requests to the Bot API. Keep your token secure and store it safely,
     * it can be used by anyone to control your bot.
     */
    String token() default StringUtils.EMPTY;

    /**
     * Non-bean implementation of {@see TokenHandler} which used for
     * more completed way than {@link this#token()}.
     * <p>
     * For example: use it to read bot token from a file
     */
    Class<? extends TokenHandler> tokenHandler() default ApplicationPropertyTokenHandler.class;

    /**
     * Non-bean implementation of {@see TokenHandler} which used for
     * more completed way than {@link this#token()}.
     * Use it when you enjoy
     * <p>
     * For example: use it to read bot token from a file
     */
    String tokenHandlerBean() default StringUtils.EMPTY;
}
