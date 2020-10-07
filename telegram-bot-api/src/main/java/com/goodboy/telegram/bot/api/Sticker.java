package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.FileId;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.meta.UniqueFileId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents a sticker.
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Sticker {

    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    @FileId
    private String id;

    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots.
     * Can't be used to download or reuse the file
     */
    @UniqueFileId
    private String uniqueId;

    /**
     * 	Video width as defined by sender
     */
    private Integer width;

    /**
     * 	Video height as defined by sender
     */
    private Integer height;

    /**
     * True, if the sticker is animated
     */
    private Boolean isAnimated;

    /**
     * Emoji associated with the sticker
     *
     * @optional
     */
    private @Optional
    String emoji;

    /**
     * Name of the sticker set to which the sticker belongs
     *
     * @optional
     */
    private @Optional String setName;

    /**
     * For mask stickers, the position where the mask should be placed
     *
     * @optional
     */
    private @Optional MaskPosition maskPosition;

    /**
     * File size
     *
     * @optional
     */
    private @Optional Integer fileSize;


    /**
     * Thumbnail of the album cover to which the music file belongs
     *
     * @optional
     */
    private @Optional PhotoSize thumb;
}
