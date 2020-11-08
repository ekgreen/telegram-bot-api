package com.goodboy.telegram.bot.spring.api.meta;

/**
 * This object represents a bot command
 */
public @interface BotCommand {

    /**
     * @return Text of the command, 1-32 characters. Can contain only lowercase English letters, digits and underscores
     */
    String command();

    /**
     * @return Description of the command, 3-256 characters.
     */
    String description() default "";
}
