package com.goodboy.telegram.bot.api.method.message;

import com.goodboy.telegram.bot.api.keyboard.ReplyMarkup;
import com.goodboy.telegram.bot.api.meta.Multipart;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.request.Uploading;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Multipart
@TelegramApi
@Accessors(chain = true)
public class SendAnimationApi {

    /**
     * Unique identifier for the target chat or username
     * of the target channel (in the format @channelusername)
     */
    private Integer chatId;

    /**
     * Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended),
     * pass an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using
     * multipart/form-data.
     *
     * @type ? = [String, InputStream, byte[], Supplier<byte[]>, Supplier<InputStream>]
     */
    private Uploading<?> animation;

    /**
     * Duration of sent video in seconds
     *
     * @optional
     */
    private @Optional Long duration;

    /**
     * Video width
     *
     * @optional
     */
    private @Optional Long width;

    /**
     * Video height
     *
     * @optional
     */
    private @Optional Long height;

    /**
     * Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     * The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should
     * not exceed 320. Ignored if the file is not uploaded using multipart/form-data.
     * <p>
     * Thumbnails can't be reused and can be only uploaded as a new file, so you can pass
     * “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under
     * <file_attach_name>.
     *
     * @type ? = [String, InputStream, byte[], Supplier<byte[]>, Supplier<InputStream>]
     * @optional
     */
    private @Optional Uploading<?> thumb;

    /**
     * Video caption (may also be used when resending photos by file_id),
     * 0-1024 characters after entities parsing
     *
     * @optional
     */
    private @Optional String caption;

    /**
     * Mode for parsing entities in the message text
     *
     * @optional
     * @see <a href="https://core.telegram.org/bots/api#formatting-options">formatting options</a>
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
