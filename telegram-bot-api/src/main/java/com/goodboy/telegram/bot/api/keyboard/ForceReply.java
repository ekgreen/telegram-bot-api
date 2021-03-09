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
 * Upon receiving a message with this object, Telegram clients will display
 * a reply interface to the user (act as if the user has selected the bot's
 * message and tapped 'Reply'). This can be extremely useful if you want
 * to create user-friendly step-by-step interfaces without having to sacrifice
 * privacy mode.
 *
 * Example: A poll bot for groups runs in privacy mode (only receives commands, replies
 * to its messages and mentions). There could be two ways to create a new poll:
 *
 * 1) Explain the user how to send a command with parameters (e.g. /newpoll question answer1
 * answer2). May be appealing for hardcore users but lacks modern day polish.
 * 2) Guide the user through a step-by-step process. 'Please send me your question',
 * 'Cool, now let's add the first answer option', 'Great. Keep adding answer options,
 * then send /done when you're ready'.
 *
 * The last option is definitely more attractive. And if you use ForceReply in your bot's
 * questions, it will receive the user's answers even if it only receives replies,
 * commands and mentions — without any extra work for the user.
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class ForceReply implements ReplyMarkup {

    /**
     *
     */
    private Boolean forceReply;

    /**
     * Use this parameter if you want to force reply from specific users only.
     *
     * Targets:
     * 1) users that are @mentioned in the text of the Message object;
     * 2) if the bot's message is a reply (has reply_to_message_id), sender of the
     * original message
     *
     * @optional
     */
    private Boolean selective;
}
