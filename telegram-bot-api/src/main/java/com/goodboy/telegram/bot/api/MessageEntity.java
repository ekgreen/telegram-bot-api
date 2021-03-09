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

/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class MessageEntity {

    /**
     * Type of the entity
     *
     * Can be “mention” (@username), “hashtag” (#hashtag), “cashtag” ($USD), “bot_command” (/start@jobs_bot),
     * “url” (https://telegram.org), “email” (do-not-reply@telegram.org), “phone_number” (+1-212-555-0123),
     * “bold” (bold text), “italic” (italic text), “underline” (underlined text), “strikethrough” (strikethrough text),
     * “code” (monowidth string), “pre” (monowidth block), “text_link” (for clickable text URLs),
     * “text_mention” (for users without usernames)
     */
    private String type;

    /**
     * Offset in UTF-16 code units to the start of the entity
     */
    private Long offset;

    /**
     * 	Length of the entity in UTF-16 code units
     */
    private Long length;

    /**
     * For “text_link” only, url that will be opened after user taps on the text
     *
     * @optional
     */
    private @Optional
    String url;

    /**
     *  For “text_mention” only, the mentioned user
     *
     *  @optional
     */
    private User user;

    /**
     * For “pre” only, the programming language of the entity text
     *
     * @optional
     */
    private String language;
}
