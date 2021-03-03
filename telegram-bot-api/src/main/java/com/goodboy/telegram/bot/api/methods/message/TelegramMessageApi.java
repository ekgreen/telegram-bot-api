package com.goodboy.telegram.bot.api.methods.message;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.platform.upload.Uploading;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


public interface TelegramMessageApi {

    /**
     * Use this method to send text messages
     *
     * @return on success, the sent Message is returned.
     */
    @NotNull TelegramCoreResponse<Message> sendMessage(@NotNull SendMessageApi request);

    /**
     * Double-argument method includes required parameter
     *
     * @param chatId unique chat identifier
     * @param text   text of the message to be sent, 1-4096 characters after entities parsing
     *
     * @see TelegramMessageApi#sendMessage(SendMessageApi)
     */
    default @NotNull TelegramCoreResponse<Message> sendMessage(@NotNull Integer chatId, @NotNull String text) {
        return sendMessage(new SendMessageApi()
                .setChatId(chatId)
                .setText(text)
        );
    }

    /**
     * Use this method to send photos.
     *
     * @return on success, the sent Message is returned.
     */
    @NotNull TelegramCoreResponse<Message> sendPhoto(@NotNull SendPhotoApi request);

    /**
     * Double-argument method includes required parameter
     *
     * @param chatId unique chat identifier
     * @param photo upload file
     *
     * @see TelegramMessageApi#sendPhoto(SendPhotoApi)
     */
    default @NotNull TelegramCoreResponse<Message> sendPhoto(@NotNull Integer chatId, @NotNull Uploading photo) {
        return sendPhoto(new SendPhotoApi()
                .setChatId(chatId)
                .setPhoto(photo)
        );
    }

    /**
     * Use this method to send audio files, if you want Telegram clients to display them in the music player. Your audio must be in the .MP3 or .M4A format.
     *
     * @return on success, the sent Message is returned.
     */
    @NotNull TelegramCoreResponse<Message> sendAudio(@NotNull SendAudioApi request);

    /**
     * Double-argument method includes required parameter
     *
     * @param chatId unique chat identifier
     * @param audio upload file
     *
     * @see TelegramMessageApi#sendAudio(SendAudioApi)
     */
    default @NotNull TelegramCoreResponse<Message> sendAudio(@NotNull Integer chatId, @NotNull Supplier<?> audio) {
        return sendAudio(new SendAudioApi()
                .setChatId(chatId)
                .setAudio(audio)
        );
    }

    /**
     * Use this method to send general files
     *
     * @return on success, the sent Message is returned.
     */
    @NotNull TelegramCoreResponse<Message> sendDocument(@NotNull SendDocumentApi request);

    /**
     * Double-argument method includes required parameter
     *
     * @param chatId unique chat identifier
     * @param document upload file
     *
     * @see TelegramMessageApi#sendDocument(SendDocumentApi)
     */
    default @NotNull TelegramCoreResponse<Message> sendDocument(@NotNull Integer chatId, @NotNull Supplier<?> document) {
        return sendDocument(new SendDocumentApi()
                .setChatId(chatId)
                .setDocument(document)
        );
    }

    /**
     * Use this method to send video files, Telegram clients support mp4 videos (other formats may be sent as Document).
     * On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size,
     * this limit may be changed in the future.
     *
     * @return on success, the sent Message is returned.
     */
    @NotNull TelegramCoreResponse<Message> sendVideo(@NotNull SendVideoApi request);

    /**
     * Double-argument method includes required parameter
     *
     * @param chatId unique chat identifier
     * @param video upload file
     *
     * @see TelegramMessageApi#sendVideo(SendVideoApi)
     */
    default @NotNull TelegramCoreResponse<Message> sendVideo(@NotNull Integer chatId, @NotNull Supplier<?> video) {
        return sendVideo(new SendVideoApi()
                .setChatId(chatId)
                .setVideo(video)
        );
    }

    /**
     * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound). On success, the sent
     * Message is returned. Bots can currently send animation files of up to 50 MB in size,
     * this limit may be changed in the future.
     *
     * @return on success, the sent Message is returned.
     */
    @NotNull TelegramCoreResponse<Message> sendAnimation(@NotNull SendAnimationApi request);

    /**
     * Double-argument method includes required parameter
     *
     * @param chatId unique chat identifier
     * @param animation upload file
     *
     * @see TelegramMessageApi#sendAnimation(SendAnimationApi)
     */
    default @NotNull TelegramCoreResponse<Message> sendAnimation(@NotNull Integer chatId, @NotNull Uploading animation) {
        return sendAnimation(new SendAnimationApi()
                .setChatId(chatId)
                .setAnimation(animation)
        );
    }

    /**
     * Use this method to send point on the map
     *
     * @return on success, the sent Message is returned.
     */
    @NotNull TelegramCoreResponse<Message> sendLocation(@NotNull SendLocationApi request);

    /**
     * Double-argument method includes required parameter
     *
     * @param chatId unique chat identifier
     * @param latitude value of latitude
     * @param longitude value of longitude
     *
     * @see TelegramMessageApi#sendLocation(SendLocationApi)
     */
    default @NotNull TelegramCoreResponse<Message> sendLocation(@NotNull Integer chatId, @NotNull Double latitude, @NotNull Double longitude) {
        return sendLocation(new SendLocationApi()
                .setChatId(chatId)
                .setLatitude(latitude)
                .setLongitude(longitude)
        );
    }

    /**
     * Use this method to send static .WEBP or animated .TGS stickers. On success, the sent Message is returned.
     *
     * @return on success, the sent Message is returned.
     */
    @NotNull TelegramCoreResponse<Message> sendSticker(@NotNull SendStickerApi request);

    /**
     * Double-argument method includes required parameter
     *
     * @param chatId unique chat identifier
     * @param sticker upload file
     *
     * @see TelegramMessageApi#sendSticker(SendStickerApi)
     */
    default @NotNull TelegramCoreResponse<Message> sendSticker(@NotNull Integer chatId, @NotNull Supplier<?> sticker) {
        return sendSticker(new SendStickerApi()
                .setChatId(chatId)
                .setSticker(sticker)
        );
    }


}
