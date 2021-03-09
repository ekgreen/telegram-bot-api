/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * This object represents a game. Use BotFather to create and edit games,
 * their short names will act as unique identifiers
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
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
    private List<MessageEntity> textEntities;

    /**
     * Animation that will be displayed in the game message in chats. Upload via BotFather
     *
     * @optional
     */
    private Animation animation;
}
