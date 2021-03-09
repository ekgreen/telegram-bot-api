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
 * This object represents a parameter of the inline keyboard button used to automatically authorize a user.
 * Serves as a great replacement for the Telegram Login Widget when the user is coming from Telegram.
 * All the user needs to do is tap/click a button and confirm that they want to log in:
 *
 * @see <a href="https://core.telegram.org/widgets/login">Telegram Login Widget</a>
 * @version 5.7.
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class LoginUrl {

    /**
     * An HTTP URL to be opened with user authorization data added to the query string when the button is pressed.
     * <p>
     * If the user refuses to provide authorization data,
     * the original URL without information about the user will be opened.
     * The data added is the same as described in Receiving authorization data.
     *
     * @see <a href="https://core.telegram.org/widgets/login#receiving-authorization-data">Receiving authorization data</a>
     * <p>
     * NOTE: You must always check the hash of the received data to verify the authentication and the integrity of
     * the data as described in Checking authorization.
     * @see <a href="https://core.telegram.org/widgets/login#checking-authorization">Checking authorization</a>
     */
    private String url;

    /**
     * New text of the button in forwarded messages
     *
     * @optional
     */
    private @Optional
    String forwardText;

    /**
     * Username of a bot, which will be used for user authorization.
     *
     * @see <a href="https://core.telegram.org/widgets/login#setting-up-a-bot">See Setting up a bot</a> for more details
     *
     * If not specified, the current bot's username will be assumed.
     * The url's domain must be the same as the domain linked with the bot.
     *
     * @see <a href="https://core.telegram.org/widgets/login#linking-your-domain-to-the-bot">Linking your domain to the bot</a> for more details
     *
     * @optional
     */
    private String botUsername;

    /**
     * Pass True to request the permission for your bot to send messages to the user
     *
     * @optional
     */
    private String requestWriteAccess;
}
