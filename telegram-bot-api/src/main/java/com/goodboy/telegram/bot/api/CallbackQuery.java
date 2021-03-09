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
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This object represents an incoming callback query from a callback button in an inline keyboard.
 * @see <a href="https://core.telegram.org/bots#inline-keyboards-and-on-the-fly-updating">inline keyboard</a>
 *
 * If the button that originated the query was attached to a message sent by the bot, the field message will be present.
 * If the button was attached to a message sent via the bot (in inline mode), the field inline_message_id will be present.
 * @see <a href="https://core.telegram.org/bots/api#inline-mode">inline mode</a>
 *
 * Exactly one of the fields data or game_short_name will be present.
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class CallbackQuery {

    /**
     * Unique identifier for this query
     */
    private String id;

    /**
     * Sender
     */
    private User from;
    /**
     * Message with the callback button that originated the query. Note that message content and message date will
     * not be available if the message is too old
     *
     * @optional
     */
    private @Optional
    Message message;

    /**
     * Identifier of the message sent via the bot in inline mode, that originated the query
     *
     * @optional
     */
    private String inlineMessageId;

    /**
     * Global identifier, uniquely corresponding to the chat to which the message with the callback button was sent.
     * Useful for high scores in games
     */
    private String chatInstance;

    /**
     * Data associated with the callback button. Be aware that a bad client can send arbitrary data in this field.
     *
     * @optional
     */
    private String data;

    /**
     * Short name of a Game to be returned, serves as the unique identifier for the game
     *
     * @optional
     */
    private String gameShortName;
}
