package com.goodboy.telegram.bot.api;

import java.time.LocalDateTime;
import java.util.List;

import com.goodboy.telegram.bot.api.keyboard.InlineKeyboardMarkup;
import com.goodboy.telegram.bot.api.meta.MessageId;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents a message
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Message {

    /**
     * Unique message identifier inside this chat
     */
    @MessageId
    private Integer id;

    /**
     * Sender, empty for messages sent to channels
     *
     * @optional
     */
    private @Optional
    User from;

    /**
     * Date the message was sent in Unix time
     */
    private LocalDateTime date;

    /**
     * Conversation the message belongs to
     */
    private Chat chat;

    /**
     * For forwarded messages, sender of the original message
     *
     * @optional
     */
    private @Optional User forwardFrom;

    /**
     * For messages forwarded from channels, identifier of the original message in the channel
     *
     * @optional
     */
    private @Optional Integer forwardFromMessageId;

    /**
     * For messages forwarded from channels, signature of the post author if present
     *
     * @optional
     */
    private @Optional String forwardSignature;

    /**
     *  Sender's name for messages forwarded from users who disallow adding a link to their account in forwarded messages
     *
     * @optional
     */
    private @Optional String forwardSenderName;

    /**
     * For forwarded messages, date the original message was sent in Unix time
     *
     * @optional
     */
    private @Optional LocalDateTime forwardDate;

    /**
     * For replies, the original message. Note that the Message object in this field will not contain further
     * reply_to_message fields even if it itself is a reply
     *
     * @optional
     */
    private @Optional Message replyToMessageId;

    /**
     * Bot through which the message was sent
     *
     * @optional
     */
    private @Optional User viaBot;

    /**
     * Date the message was last edited in Unix time
     *
     * @optional
     */
    private @Optional LocalDateTime editDate;

    /**
     * The unique identifier of a media message group this message belongs to
     *
     * @optional
     */
    private @Optional String mediaGroupId;

    /**
     * Signature of the post author for messages in channels
     *
     * @optional
     */
    private @Optional String authorSignature;

    /**
     * For text messages, the actual UTF-8 text of the message, 0-4096 characters
     *
     * @optional
     */
    private @Optional String text;

    /**
     * For text messages, special entities like usernames, URLs, bot commands, etc. that appear in the text
     *
     * @optional
     */
    private @Optional List<MessageEntity> entities;

    /**
     * Message is an animation, information about the animation. For backward compatibility, when this field is set,
     * the document field will also be set
     *
     * @optional
     */
    private @Optional Animation animation;

    /**
     * Message is an audio file, information about the file
     *
     * @optional
     */
    private @Optional Audio audio;

    /**
     * Message is a general file, information about the file
     *
     * @optional
     */
    private @Optional Document document;

    /**
     * Message is a photo, available sizes of the photo
     *
     * @optional
     */
    private @Optional List<PhotoSize> photo;

    /**
     * Message is a sticker, information about the sticker
     *
     * @optional
     */
    private @Optional Sticker sticker;

    /**
     * Message is a video, information about the video
     *
     * @optional
     */
    private @Optional Video video;

    /**
     * Message is a video note, information about the video message
     *
     * @optional
     */
    private @Optional VideoNote videoNote;

    /**
     * Message is a voice message, information about the file
     *
     * @optional
     */
    private @Optional Voice voice;

    /**
     * Caption for the animation, audio, document, photo, video or voice, 0-1024 characters
     *
     * @optional
     */
    private @Optional String caption;

    /**
     * For messages with a caption, special entities like usernames, URLs, bot commands, etc. that appear in the caption
     *
     * @optional
     */
    private @Optional List<MessageEntity> captionEntities;

    /**
     * Message is a shared contact, information about the contact
     *
     * @optional
     */
    private @Optional Contact contact;

    /**
     * Message is a dice with random value from 1 to 6
     *
     * @optional
     */
    private @Optional Dice dice;

    /**
     * Message is a game, information about the game. More about games
     *
     * @optional
     */
    private @Optional Game game;

    /**
     * Message is a native poll, information about the poll
     *
     * @optional
     */
    private @Optional Poll poll;

    /**
     * Message is a venue, information about the venue. For backward compatibility, when this field is set,
     * the location field will also be set
     *
     * @optional
     */
    private @Optional Venue venue;

    /**
     * Message is a shared location, information about the location
     *
     * @optional
     */
    private @Optional Location location;

    /**
     * New members that were added to the group or supergroup and information about them (the bot itself may be one of
     * these members)
     *
     * @optional
     */
    private @Optional List<User> newChatMembers;


    /**
     * A member was removed from the group, information about them (this member may be the bot itself)
     *
     * @optional
     */
    private @Optional User leftChatMember;


    /**
     * A chat title was changed to this value
     *
     * @optional
     */
    private @Optional String newChatTitle;


    /**
     * A chat photo was change to this value
     *
     * @optional
     */
    private @Optional List<PhotoSize> newChatPhoto;

    /**
     * Service message: the chat photo was deleted
     *
     * @optional
     */
    private @Optional Boolean deleteChatPhoto;


    /**
     * Service message: the chat photo was create
     *
     * @optional
     */
    private @Optional Boolean groupChatCreated;


    /**
     * Service message: the supergroup has been created. This field can't be received in a message coming through updates,
     * because bot can't be a member of a supergroup when it is created. It can only be found in reply_to_message
     * if someone replies to a very first message in a directly created supergroup
     *
     * @optional
     */
    private @Optional Boolean supergroupChatCreated;


    /**
     * Service message: the channel has been created. This field can't be received in a message coming through updates,
     * because bot can't be a member of a channel when it is created. It can only be found in reply_to_message if
     * someone replies to a very first message in a channel
     *
     * @optional
     */
    private @Optional Boolean channelChatCreated;


    /**
     * The group has been migrated to a supergroup with the specified identifier. This number may be greater than
     * 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller
     * than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier
     *
     * @optional
     */
    private @Optional Integer migrateToChatId;


    /**
     * The supergroup has been migrated from a group with the specified identifier. This number may be greater than
     * 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller
     * than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier
     *
     * @optional
     */
    private @Optional Integer migrateFromChatId;


    /**
     * Specified message was pinned. Note that the Message object in this field will not contain further
     * reply_to_message fields even if it is itself a reply
     *
     * @optional
     */
    private @Optional Message pinnedMessage;


    /**
     * Message is an invoice for a payment, information about the invoice
     *
     * @see <a href="https://core.telegram.org/bots/api#payments">More about payments</a>
     * @optional
     */
    private @Optional Invoice invoice;


    /**
     * Message is a service message about a successful payment, information about the payment
     *
     * @see <a href="https://core.telegram.org/bots/api#payments">More about payments</a>
     * @optional
     */
    private @Optional SuccessfulPayment successfulPayment;


    /**
     * The domain name of the website on which the user has logged in
     *
     * @see <a href="https://core.telegram.org/widgets/login">More about Telegram Login</a>
     * @optional
     */
    private @Optional String connectedWebsite;


    /**
     * Telegram Passport data
     * @optional
     */
    private @Optional PassportData passportData;


    /**
     * Inline keyboard attached to the message. login_url buttons are represented as ordinary url buttons
     *
     * @optional
     */
    private @Optional
    InlineKeyboardMarkup replyMarkup;
}
