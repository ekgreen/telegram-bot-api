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

import com.goodboy.telegram.bot.api.meta.FileId;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.meta.UniqueFileId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents a sticker.
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Sticker {

    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    @FileId
    private String id;

    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots.
     * Can't be used to download or reuse the file
     */
    @UniqueFileId
    private String uniqueId;

    /**
     * 	Video width as defined by sender
     */
    private Integer width;

    /**
     * 	Video height as defined by sender
     */
    private Integer height;

    /**
     * True, if the sticker is animated
     */
    private Boolean isAnimated;

    /**
     * Emoji associated with the sticker
     *
     * @optional
     */
    private @Optional
    String emoji;

    /**
     * Name of the sticker set to which the sticker belongs
     *
     * @optional
     */
    private @Optional String setName;

    /**
     * For mask stickers, the position where the mask should be placed
     *
     * @optional
     */
    private @Optional MaskPosition maskPosition;

    /**
     * File size
     *
     * @optional
     */
    private @Optional Integer fileSize;


    /**
     * Thumbnail of the album cover to which the music file belongs
     *
     * @optional
     */
    private @Optional PhotoSize thumb;
}
