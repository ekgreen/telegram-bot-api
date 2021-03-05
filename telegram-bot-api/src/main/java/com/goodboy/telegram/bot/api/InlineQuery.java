package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This object represents an incoming inline query. When the user sends an empty query, your bot could return some
 * default or trending results
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class InlineQuery {

    /**
     * Unique identifier for this query
     */
    private String id;

    /**
     * Sender
     */
    private User update;

    /**
     * Sender location, only for bots that request user location
     *
     * @optional
     */
    private @Optional Location location;

    /**
     * Text of the query (up to 256 characters)
     */
    private String query;

    /**
     * Offset of the results to be returned, can be controlled by the bot
     */
    private String offset;
}
