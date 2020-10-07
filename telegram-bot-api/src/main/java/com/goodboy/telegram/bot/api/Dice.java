package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * This object represents an animated emoji that displays a random value
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Dice {

    /**
     * Emoji on which the dice throw animation is based
     */
    private String emoji;

    /**
     * Value of the dice, 1-6 for “🎲” and “🎯” base emoji, 1-5 for “🏀” base emoji
     */
    private Integer value;
}
