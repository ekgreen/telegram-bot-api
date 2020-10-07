package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents an issue in an unspecified place. The error is considered resolved when new data is added
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class PassportElementErrorUnspecified {

    /**
     * Error source, must be unspecified
     */
    private String source;

    /**
     * Type of element of the user's Telegram Passport which has the issue
     */
    private String type;

    /**
     * Base64-encoded element hash
     */
    private String elementHash;

    /**
     * Error message
     */
    private String message;
}
