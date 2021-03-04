package com.goodboy.telegram.bot.api;

import java.util.List;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This object contains information about a poll
 */
@TelegramApi
@Data
@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
public class Poll {

    /**
     * Unique poll identifier
     */
    private String id;

    /**
     * Poll question, 1-255 characters
     */
    private String question;

    /**
     * List of poll options
     */
    private List<PollOption> options;

    /**
     * Total number of users that voted in the poll
     */
    private Integer totalVoterCount;

    /**
     * True, if the poll is closed
     */
    private Boolean isClosed;

    /**
     * True, if the poll is anonymous
     */
    private Boolean isAnonymous;

    /**
     * Poll type, currently can be “regular” or “quiz”
     */
    private String type;

    /**
     * True, if the poll allows multiple answers
     */
    private Boolean allowsMultipleAnswers;

    /**
     *  0-based identifier of the correct answer option.
     *  Available only for polls in the quiz mode, which are closed, or was sent (not forwarded) by the bot
     *  or to the private chat with the bot
     *
     * @optional
     */
    private @Optional
    Integer correctOptionId;

    /**
     * Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll,
     * 0-200 characters
     *
     * @optional
     */
    private @Optional String explanation;

    /**
     * Special entities like usernames, URLs, bot commands, etc. that appear in the explanation
     *
     * @optional
     */
    private @Optional List<MessageEntity> explanationEntities;

    /**
     * Amount of time in seconds the poll will be active after creation
     *
     * @optional
     */
    private @Optional Integer openPeriod;

    /**
     * Point in time (Unix timestamp) when the poll will be automatically closed
     *
     * @optional
     */
    private @Optional Integer closeDate;

}
