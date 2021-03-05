package com.goodboy.telegram.bot.api;


import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represents a result of an inline query that was chosen by the user and sent to their chat partner
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class ChosenInlineResult {

    /**
     * The unique identifier for the result that was chosen
     */
    private String id;

    /**
     * 	The user that chose the result
     */
    private User from;

    /**
     * Sender location, only for bots that request user location
     *
     * @optional
     */
    private @Optional
    Location location;

    /**
     * Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message.
     * Will be also received in callback queries and can be used to edit the message
     */
    private String inlineMessageId;

    /**
     * The query that was used to obtain the result
     */
    private String query;
}
