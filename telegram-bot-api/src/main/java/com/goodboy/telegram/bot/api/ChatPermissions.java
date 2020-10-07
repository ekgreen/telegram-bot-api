package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Describes actions that a non-administrator user is allowed to take in a chat
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class ChatPermissions {

    /**
     * True, if the user is allowed to send text messages, contacts, locations and venues
     *
     * @optional
     */
    private @Optional
    Boolean canSendMessages;

    /**
     * True, if the user is allowed to send audios, documents, photos, videos, video notes and voice notes, implies can_send_messages
     *
     * @optional
     */
    private @Optional Boolean canSendMediaMessages;

    /**
     * True, if the user is allowed to send polls, implies can_send_messages
     *
     * @optional
     */
    private @Optional Boolean canSendPolls;

    /**
     * True, if the user is allowed to send animations, games, stickers and use inline bots,
     * implies can_send_media_messages
     *
     * @optional
     */
    private @Optional Boolean canSendOtherMessages;

    /**
     *  True, if the user is allowed to add web page previews to their messages, implies can_send_media_messages
     *
     * @optional
     */
    private @Optional Boolean canAddWebPagePreviews;

    /**
     * True, if the user is allowed to change the chat title, photo and other settings. Ignored in @TelegramApi
@Data
@Accessors(chain = true)
public supergroups
     *
     * @optional
     */
    private @Optional Boolean canChangeInfo;

    /**
     * True, if the user is allowed to invite new users to the chat
     *
     * @optional
     */
    private @Optional Boolean canInviteUsers;

    /**
     * True, if the user is allowed to pin messages. Ignored in @TelegramApi
@Data
@Accessors(chain = true)
public supergroups
     *
     * @optional
     */
    private @Optional Boolean canPinMessages;
}
