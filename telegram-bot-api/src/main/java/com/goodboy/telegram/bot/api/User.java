package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This object represents a Telegram user or bot
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class User {

    /**
     * Unique identifier for this user or bot
     */
    private Integer id;

    /**
     * True, if this user is a bot
     */
    private Boolean isBot;
    /**
     * User's or bot's first name
     */
    private String firstName;
    /**
     * User's or bot's last name
     *
     * @optional
     */
    private @Optional
    String lastName;

    /**
     * User's or bot's username
     *
     * @optional
     */
    private @Optional String username;

    /**
     *  IETF language tag of the user's language
     *
     * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF</a>
     * @optional
     */
    private @Optional String languageCode;

    /**
     * True, if the bot can be invited to groups. Returned only in getMe
     *
     * @optional
     */
    private @Optional Boolean canJoinGroups;

    /**
     * True, if privacy mode is disabled for the bot. Returned only in getMe
     *
     * @optional
     */
    private @Optional Boolean canReadAllGroupMessages;

    /**
     * True, if the bot supports inline queries. Returned only in getMe
     *
     * @optional
     */
    private @Optional Boolean supportsInlineQueries;

}
