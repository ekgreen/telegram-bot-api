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
 * This object represents one size of a photo or a file / sticker thumbnail
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class PhotoSize {

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
     * Photo width
     */
    private Long width;

    /**
     * Photo height
     */
    private Long height;

    /**
     * File size
     *
     * @optional
     */
    private @Optional
    Long fileSize;
}
