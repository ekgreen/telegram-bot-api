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
 * This object represents a sticker set
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class StickerSet {

    /**
     * Sticker set name
     */
    private String name;

    /**
     * Sticker set title
     */
    private String title;

    /**
     * True, if the sticker set contains animated stickers
     */
    private Boolean isAnimated;

    /**
     * True, if the sticker set contains masks
     */
    private Boolean containsMasks;


    /**
     * List of all set stickers
     */
    private List<Sticker> stickers;


    /**
     * Sticker set thumbnail in the .WEBP or .TGS format
     *
     * @optional
     */
    private @Optional PhotoSize thumb;
}