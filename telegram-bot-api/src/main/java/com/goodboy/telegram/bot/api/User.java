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
 * This object represents a Telegram user or bot
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class User {

    /**
     * Unique identifier for this user or bot
     */
    private Integer id;

    /**
     * True, if this user is a bot
     */
    private Boolean isBot;
    /**
     * User's or bot's first name
     */
    private String firstName;
    /**
     * User's or bot's last name
     *
     * @optional
     */
    private @Optional
    String lastName;

    /**
     * User's or bot's username
     *
     * @optional
     */
    private @Optional String username;

    /**
     *  IETF language tag of the user's language
     *
     * @see <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF</a>
     * @optional
     */
    private @Optional String languageCode;

    /**
     * True, if the bot can be invited to groups. Returned only in getMe
     *
     * @optional
     */
    private @Optional Boolean canJoinGroups;

    /**
     * True, if privacy mode is disabled for the bot. Returned only in getMe
     *
     * @optional
     */
    private @Optional Boolean canReadAllGroupMessages;

    /**
     * True, if the bot supports inline queries. Returned only in getMe
     *
     * @optional
     */
    private @Optional Boolean supportsInlineQueries;

}
