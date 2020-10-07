package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * This object represents a game. Use BotFather to create and edit games,
 * their short names will act as unique identifiers
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Game {

    /**
     * Title of the game
     */
    private String title;

    /**
     * Description of the game
     */
    private String description;

    /**
     * Photo that will be displayed in the game message in chats
     */
    private List<PhotoSize> photo;

    /**
     * Brief description of the game or high scores included in the game message.
     * Can be automatically edited to include current high scores for the game when the bot calls setGameScore,
     * or manually edited using editMessageText.
     *
     * 0-4096 characters
     *
     * @optional
     */
    private @Optional
    String text;

    /**
     * Special entities that appear in text, such as usernames, URLs, bot commands, etc
     *
     * @optional
     */
    private @Optional List<MessageEntity> textEntities;

    /**
     * Animation that will be displayed in the game message in chats. Upload via BotFather
     *
     * @optional
     */
    private @Optional Animation animation;
}
