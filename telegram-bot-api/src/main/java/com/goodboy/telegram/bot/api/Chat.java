package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This object represents a chat
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
    private Integer id;

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
    private @Optional String username;

    /**
     * First name of the other party in a private chat
     *
     * @optional
     */
    private @Optional String firstName;

    /**
     * Last name of the other party in a private chat
     *
     * @optional
     */
    private @Optional String lastName;

    /**
     * Chat photo. Returned only in getChat
     *
     * @optional
     */
    private @Optional ChatPhoto photo;

    /**
     * Description, for groups, supergroups and channel chats. Returned only in getChat
     *
     * @optional
     */
    private @Optional String description;

    /**
     * Chat invite link, for groups, supergroups and channel chats. Each administrator in a chat generates their
     * own invite links, so the bot must first generate the link using exportChatInviteLink. Returned only in getChat
     *
     * @optional
     */
    private @Optional String inviteLink;

    /**
     *  Pinned message, for groups, supergroups and channels. Returned only in getChat
     *
     * @optional
     */
    private @Optional Message pinnedMessage;

    /**
     * Default chat member permissions, for groups and supergroups. Returned only in getChat
     *
     * @optional
     */
    private @Optional ChatPermissions permissions;

    /**
     * For supergroups, the minimum allowed delay between consecutive messages sent by each unpriviledged user.
     * Returned only in getChat
     *
     * @optional
     */
    private @Optional Integer slowModeDelay;

    /**
     * For supergroups, name of group sticker set. Returned only in getChat
     *
     * @optional
     */
    private @Optional String stickerSetName;

    /**
     * True, if the bot can change the group sticker set. Returned only in getChat
     *
     * @optional
     */
    private @Optional Boolean canSetStickerSet;

}
