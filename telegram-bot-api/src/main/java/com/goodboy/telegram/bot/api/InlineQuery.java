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
 * This object represents an incoming inline query. When the user sends an empty query, your bot could return some
 * default or trending results
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class InlineQuery {

    /**
     * Unique identifier for this query
     */
    private String id;

    /**
     * Sender
     */
    private User update;

    /**
     * Sender location, only for bots that request user location
     *
     * @optional
     */
    private Location location;

    /**
     * Text of the query (up to 256 characters)
     */
    private String query;

    /**
     * Offset of the results to be returned, can be controlled by the bot
     */
    private String offset;
}
