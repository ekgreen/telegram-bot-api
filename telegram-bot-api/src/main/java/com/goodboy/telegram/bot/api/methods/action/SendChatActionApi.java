package com.goodboy.telegram.bot.api.methods.action;

import com.goodboy.telegram.bot.api.meta.ApiQuery;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.methods.Api;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition.SEND_CHAT_ACTION_CALL_METHOD;

/**
 * Use this method when you need to tell the user that something is happening on the bot's side. The status is set for
 * 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status). Returns True on
 * success.
 *
 * Example: The ImageBot needs some time to process a request and upload the image. Instead of sending a text message
 * along the lines of “Retrieving image, please wait…”, the bot may use sendChatAction with action = upload_photo.
 * The user will see a “sending photo” status for the bot.
 *
 * We only recommend using this method when a response from the bot will take a noticeable amount of time to arrive
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Data
@TelegramApi
@Accessors(chain = true)
@ApiQuery(method = SEND_CHAT_ACTION_CALL_METHOD, provides = Boolean.class)
public class SendChatActionApi implements Api {

    /**
     * Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    public final @Nonnull String chatId;

    /**
     * Type of action to broadcast. Choose one, depending on what the user is about to receive: typing for text
     * messages, upload_photo for photos, record_video or upload_video for videos, record_voice or upload_voice
     * for voice notes, upload_document for general files, find_location for location data, record_video_note or
     * upload_video_note for video notes.
     */
    public final @Nonnull Action action;
}
