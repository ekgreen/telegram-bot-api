package com.goodboy.telegram.bot.api.methods.message;

import com.goodboy.telegram.bot.api.keyboard.ReplyMarkup;
import com.goodboy.telegram.bot.api.meta.Multipart;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.platform.upload.Uploading;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Multipart
@TelegramApi
@Accessors(chain = true)
public class SendPhotoApi implements Api {

    /**
     * Unique identifier for the target chat or username
     * of the target channel (in the format @channelusername)
     */
    private Integer  chatId;

    /**
     * 	Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended),
     * 	pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using
     * 	multipart/form-data.
     *
     * @type ? = [String, InputStream, byte[]]
     */
    private Uploading photo;

    /**
     * Photo caption (may also be used when resending photos by file_id),
     * 0-1024 characters after entities parsing
     *
     * @optional
     */
    private @Optional String caption;

    /**
     * Mode for parsing entities in the message text
     *
     * @see <a href="https://core.telegram.org/bots/api#formatting-options">formatting options</a>
     * @optional
     */
    private @Optional String parseMode;

    /**
     * Sends the message silently. Users will receive a notification with no sound
     *
     * @optional
     */
    private @Optional Boolean disableNotification;

    /**
     * If the message is a reply, ID of the original message
     *
     * @optional
     */
    private @Optional Integer replyToMessageId;

    /**
     * Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     * instructions to remove reply keyboard or to force a reply from the user.
     *
     * @optional
     */
    private @Optional ReplyMarkup replyMarkup;
}
