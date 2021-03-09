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
 * This object represents a chat
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Chat {

    /**
     * Unique identifier for this chat. This number may be greater than 32 bits and some programming languages
     * may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64
     * bit integer or double-precision float type are safe for storing this identifier.
     */
    private Long id;

    /**
     * Type of chat, can be either “private”, “group”, “supergroup” or “channel”
     */
    private String type;

    /**
     * Title, for supergroups, channels and group chats
     *
     * @optional
     */
    private @Optional
    String title;

    /**
     * Username, for private chats, supergroups and channels if available
     *
     * @optional
     */
    private String username;

    /**
     * First name of the other party in a private chat
     *
     * @optional
     */
    private String firstName;

    /**
     * Last name of the other party in a private chat
     *
     * @optional
     */
    private String lastName;

    /**
     * Chat photo. Returned only in getChat
     *
     * @optional
     */
    private ChatPhoto photo;

    /**
     * Description, for groups, supergroups and channel chats. Returned only in getChat
     *
     * @optional
     */
    private String description;

    /**
     * Chat invite link, for groups, supergroups and channel chats. Each administrator in a chat generates their
     * own invite links, so the bot must first generate the link using exportChatInviteLink. Returned only in getChat
     *
     * @optional
     */
    private String inviteLink;

    /**
     *  Pinned message, for groups, supergroups and channels. Returned only in getChat
     *
     * @optional
     */
    private Message pinnedMessage;

    /**
     * Default chat member permissions, for groups and supergroups. Returned only in getChat
     *
     * @optional
     */
    private ChatPermissions permissions;

    /**
     * For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user.
     * Returned only in getChat
     *
     * @optional
     */
    private Long slowModeDelay;

    /**
     * For supergroups, name of group sticker set. Returned only in getChat
     *
     * @optional
     */
    private String stickerSetName;

    /**
     * True, if the bot can change the group sticker set. Returned only in getChat
     *
     * @optional
     */
    private Boolean canSetStickerSet;

}
