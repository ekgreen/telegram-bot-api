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
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This object represents an incoming update.
 * At most one of the optional parameters can be present in any given update
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Update {

    /**
     * The update's unique identifier. Update identifiers start from a certain positive number and increase sequentially.
     * This ID becomes especially handy if you're using Webhooks, since it allows you to ignore repeated updates or
     * to restore the correct update sequence, should they get out of order. If there are no new updates for at least
     * a week, then identifier of the next update will be chosen randomly instead of sequentially.
     *
     * @see <a href="https://core.telegram.org/bots/api#setwebhook">Webhooks</a>
     */
    private Integer id;

    /**
     * New incoming message of any kind — text, photo, sticker, etc
     *
     * @optional
     */
    private @Optional Message message;

    /**
     * New version of a message that is known to the bot and was edited
     *
     * @optional
     */
    private @Optional Message editedMessage;

    /**
     * New incoming channel post of any kind — text, photo, sticker, etc
     *
     * @optional
     */
    private @Optional Message channelPost;

    /**
     * New version of a channel post that is known to the bot and was edited
     *
     * @optional
     */
    private @Optional Message editedChannelPost;

    /**
     * New incoming inline query
     *
     * @optional
     */
    private @Optional InlineQuery inlineQuery;

    /**
     * The result of an inline query that was chosen by a user and sent to their chat partner.
     * Please see our documentation on the feedback collecting for details on how to enable these updates for your bot.
     *
     * @optional
     */
    private @Optional ChosenInlineResult chosenInlineResult;

    /**
     * New incoming callback query
     *
     * @optional
     */
    private @Optional CallbackQuery callbackQuery;

    /**
     * New incoming shipping query. Only for invoices with flexible price
     *
     * @optional
     */
    private @Optional ShippingQuery shippingQuery;

    /**
     * New incoming pre-checkout query. Contains full information about checkout
     *
     * @optional
     */
    private @Optional PreCheckoutQuery preCheckoutQuery;

    /**
     * New poll state. Bots receive only updates about stopped polls and polls, which are sent by the bot
     *
     * @optional
     */
    private @Optional Poll poll;

    /**
     * A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by the bot itself
     *
     * @optional
     */
    private @Optional PollAnswer pollAnswer;

}
