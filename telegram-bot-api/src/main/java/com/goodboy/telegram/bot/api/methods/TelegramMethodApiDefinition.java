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

package com.goodboy.telegram.bot.api.methods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramMethodApiDefinition {

    /**
     * Default endpoint is <a href="https://api.telegram.org/">telegram</a>
     * Will replaced by null host value
     */
    public static final String DEFAULT_HOST = "https://api.telegram.org";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to specify a url and get info about chat
     */
    public final static String GET_ME = "getMe";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to change the list of the bot's commands
     */
    public final static String SET_MY_COMMANDS = "setMyCommands";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to get the current list of the bot's commands
     */
    public final static String GET_MY_COMMANDS = "getMyCommands";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to send text messages
     */
    public final static String SEND_MESSAGE_CALL_METHOD = "sendMessage";

    /**
     * Use this method to send photos. On success, the sent Message is returned.
     */
    public final static String SEND_PHOTO_CALL_METHOD = "sendPhoto";

    /**
     * Use this method to send video files, Telegram clients support mp4 videos (other formats may be sent as Document).
     * On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size,
     * this limit may be changed in the future.
     */
    public final static String SEND_VIDEO_CALL_METHOD = "sendVideo";

    /**
     * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound).
     * On success, the sent Message is returned. Bots can currently send animation files of up to 50 MB in size,
     * this limit may be changed in the future.
     */
    public final static String SEND_ANIMATION_CALL_METHOD = "sendAnimation";

    /**
     * Use this method to send audio files, if you want Telegram clients to display them in the music player.
     * Your audio must be in the .MP3 or .M4A format. On success, the sent Message is returned.
     * Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
     * <p>
     * For sending voice messages, use the sendVoice method instead.
     */
    public final static String SEND_AUDIO_CALL_METHOD = "sendAudio";

    /**
     * Use this method to send audio files, if you want Telegram clients to display them in the music player.
     * Your audio must be in the .MP3 or .M4A format. On success, the sent Message is returned.
     * Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
     * <p>
     * For sending voice messages, use the sendVoice method instead.
     */
    public final static String SEND_DOCUMENT_CALL_METHOD = "sendDocument";

    /**
     * Use this method to send static .WEBP or animated .TGS stickers.
     * On success, the sent Message is returned.
     */
    public final static String SEND_STICKER_CALL_METHOD = "sendSticker";

    /**
     * As of v.4.0, Telegram clients support rounded square mp4 videos of up to 1 minute long.
     * Use this method to send video messages. On success, the sent Message is returned.
     */
    public final static String SEND_VIDEO_NOTE_CALL_METHOD = "sendVideoNote";

    /**
     * Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message.
     * For this to work, your audio must be in an .OGG file encoded with OPUS (other formats may be sent as Audio or Document).
     * On success, the sent Message is returned. Bots can currently send voice messages of up to 50 MB in size,
     * this limit may be changed in the future.
     */
    public final static String SEND_VOICE_CALL_METHOD = "sendVoice";

    /**
     * Use this method to send point on the map. On success, the sent Message is returned.
     */
    public final static String SEND_LOCATION_CALL_METHOD = "sendLocation";

    /**
     * Use this method to send information about a venue. On success, the sent Message is returned.
     */
    public final static String SEND_VENUE_CALL_METHOD = "sendVenue";

    /**
     * Use this method to send phone contacts. On success, the sent Message is returned.
     */
    public final static String SEND_CONTACT_CALL_METHOD = "sendContact";

    /**
     * Use this method to send a native poll. On success, the sent Message is returned.
     */
    public final static String SEND_POLL_CALL_METHOD = "sendPoll";

    /**
     * Use this method to send an animated emoji that will display a random value.
     * On success, the sent Message is returned.
     */
    public final static String SEND_DICE_CALL_METHOD = "sendDice";

    /**
     * Use this method to send invoices. On success, the sent Message is returned.
     */
    public final static String SEND_INVOICE_CALL_METHOD = "sendInvoice";

    /**
     * Use this method to send a game. On success, the sent Message is returned.
     */
    public final static String SEND_GAME_CALL_METHOD = "sendGame";

    /**
     * Use this method to send a group of photos, videos, documents or audios as an album.
     * Documents and audio files can be only grouped in an album with messages of the same type.
     * On success, an array of Messages that were sent is returned.
     */
    public final static String SEND_MEDIA_GROUP_CALL_METHOD = "sendMediaGroup";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to specify a url and receive incoming updates via an outgoing webhook.
     */
    public final static String SET_WEBHOOK_CALL_METHOD = "setWebhook";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to remove webhook integration if you decide to switch back to getUpdates.
     */
    public final static String DELETE_WEBHOOK_CALL_METHOD = "deleteWebhook";

    /**
     * Part of http uri path. Based on telegram bot api.
     * Use this method to get current webhook status. Requires no parameters.
     */
    public final static String GET_WEBHOOK_INFO_CALL_METHOD = "getWebhookInfo";

}
