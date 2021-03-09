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

/**
 * This object represents one button of the reply keyboard.
 * For simple text buttons String can be used instead of this object
 * to specify text of the button. Optional fields request_contact, request_location,
 * and request_poll are mutually exclusive.
 *
 * Note: request_contact and request_location options will only work in Telegram versions
 * released after 9 April, 2016. Older clients will display unsupported message.
 *
 * Note: request_poll option will only work in Telegram versions released after
 * 23 January, 2020. Older clients will display unsupported message.
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class KeyboardButton {

    /**
     * Text of the button. If none of the optional fields are used, it will be sent
     * as a message when the button is pressed
     */
    private String text;

    /**
     * If True, the user's phone number will be sent as a contact when
     * the button is pressed. Available in private chats only
     *
     * @optional
     */
    private Boolean requestContact;

    /**
     * If True, the user's current location will be sent when the button is pressed.
     * Available in private chats only
     *
     * @optional
     */
    private Boolean requestLocation;

    /**
     * If specified, the user will be asked to create a poll and send it to
     * the bot when the button is pressed. Available in private chats only
     *
     * @optional
     */
    private KeyboardButtonPollType requestPoll;
}
