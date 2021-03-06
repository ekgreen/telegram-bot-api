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

package com.goodboy.telegram.bot.api.methods.message.stickers;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.StickerSet;
import com.goodboy.telegram.bot.api.meta.ApiQuery;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

/**
 * Use this method to get a sticker set. On success, a StickerSet object is returned.
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Data
@TelegramApi
@Accessors(chain = true)
@ApiQuery(method = TelegramMethodApiDefinition.GET_STICKER_SET_CALL_METHOD, provides = StickerSet.class)
public class GetStickerSetApi implements Api {

    /**
     * Name of the sticker set
     */
    private @Nonnull String name;
}
