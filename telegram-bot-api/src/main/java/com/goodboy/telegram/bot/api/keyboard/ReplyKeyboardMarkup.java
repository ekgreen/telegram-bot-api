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

package com.goodboy.telegram.bot.api.keyboard;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
 @TelegramApi
@Data
@Accessors(chain = true)
public class ReplyKeyboardMarkup implements ReplyMarkup {

    /**
     * Array of button rows, each represented by an Array of KeyboardButton objects
     */
    private List<List<KeyboardButton>> keyboard;

    /**
     * Requests clients to resize the keyboard vertically for optimal
     * fit (e.g., make the keyboard smaller if there are just two rows of buttons).
     * Defaults to false, in which case the custom keyboard is always of the same
     * height as the app's standard keyboard
     *
     * @optional
     */
    private @Optional Boolean resizeKeyboard;

    /**
     * Requests clients to hide the keyboard as soon as it's been used.
     * The keyboard will still be available, but clients will automatically
     * display the usual letter-keyboard in the chat – the user can press
     * a special button in the input field to see the custom keyboard again.
     *
     * Defaults to false
     *
     * @optional
     */
    private @Optional Boolean oneTimeKeyboard;

    /**
     * Use this parameter if you want to show the keyboard to specific users only.
     * Targets:
     * 1) users that are @mentioned in the text of the Message object;
     * 2) if the bot's message is a reply (has reply_to_message_id), sender of the original
     * message
     *
     * @optional
     */
    private @Optional Boolean selective;

}
