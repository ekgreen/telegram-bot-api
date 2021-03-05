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

import com.goodboy.telegram.bot.api.CallableGame;
import com.goodboy.telegram.bot.api.LoginUrl;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents one button of an inline keyboard. You must use exactly one of the optional fields
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class InlineKeyboardButton {

    /**
     * Label text on the button
     */
    private String text;

    /**
     * HTTP or tg:// url to be opened when button is pressed
     *
     * @optional
     */
    private @Optional
    String url;

    /**
     * An HTTP URL used to automatically authorize the user. Can be used as a replacement for the Telegram Login Widget
     *
     * @optional
     */
    private @Optional
    LoginUrl loginUrl;

    /**
     * If set, pressing the button will prompt the user to select one of their chats, open that chat and insert
     * the bot's username and the specified inline query in the input field. Can be empty, in which case just the
     * bot's username will be inserted
     *
     * Note: This offers an easy way for users to start using your bot in inline mode when they are currently
     * in a private chat with it. Especially useful when combined with switch_pm… actions – in this case
     * the user will be automatically returned to the chat they switched from, skipping the chat selection screen.
     *
     * @optional
     */
    private @Optional String callbackData;

    /**
     * If set, pressing the button will insert the bot's username and the specified inline query in the current chat's
     * input field. Can be empty, in which case only the bot's username will be inserted
     *
     * @optional
     */
    private @Optional String switchInlineQuery;

    /**
     * This offers a quick way for the user to open your bot in inline mode in the same chat – good for selecting
     * something from multiple options
     *
     * @optional
     */
    private @Optional String switchInlineQueryCurrentChat;

    /**
     * Description of the game that will be launched when the user presses the button.
     *
     * NOTE: This type of button must always be the first button in the first row.
     *
     * @optional
     */
    private @Optional
    CallableGame callbackGame;

    /**
     * Specify True, to send a Pay button.
     *
     * @see <a href="https://core.telegram.org/bots/api#payments">Pay button</a>
     *
     * NOTE: This type of button must always be the first button in the first row.
     *
     * @optional
     */
    private @Optional Boolean pay;
}
