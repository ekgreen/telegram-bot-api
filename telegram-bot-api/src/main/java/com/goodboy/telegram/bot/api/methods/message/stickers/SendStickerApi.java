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
import com.goodboy.telegram.bot.api.keyboard.ReplyMarkup;
import com.goodboy.telegram.bot.api.meta.*;
import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.api.platform.entry.Uploading;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Supplier;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Data
@TelegramApi
@Accessors(chain = true)
@ApiQuery(method = TelegramMethodApiDefinition.SEND_STICKER_CALL_METHOD, provides = Message.class)
public class SendStickerApi implements Api {

    /**
     * Unique identifier for the target chat or username
     * of the target channel (in the format @channelusername)
     */
    private String chatId;

    /**
     * Sticker to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended),
     * pass an HTTP URL as a String for Telegram to get a .WEBP file from the Internet, or upload a new one using
     * multipart/form-data.
     */
    private Uploading sticker;

    /**
     * Sends the message silently. Users will receive a notification with no sound
     *
     * @optional
     */
    private Boolean disableNotification;

    /**
     * If the message is a reply, ID of the original message
     *
     * @optional
     */
    private Long replyToMessageId;

    /**
     * Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     * instructions to remove reply keyboard or to force a reply from the user.
     *
     * @optional
     */
    private ReplyMarkup replyMarkup;
}
