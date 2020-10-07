package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object contains information about one answer option in a poll
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class PollOption {

    /**
     * Option text, 1-100 characters
     */
    private String text;

    /**
     * Number of users that voted for this option
     */
    private Integer voterCount;
}
