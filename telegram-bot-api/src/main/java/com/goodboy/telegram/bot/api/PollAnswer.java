package com.goodboy.telegram.bot.api;

import java.util.List;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents an answer of a user in a non-anonymous poll
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class PollAnswer {

    /**
     * Unique poll identifier
     */
    private String pollId;

    /**
     * The user, who changed the answer to the poll
     */
    private User user;

    /**
     * 0-based identifiers of answer options, chosen by the user. May be empty if the user retracted their vote.
     */
    private List<Integer> optionIds;
}
