package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.FileId;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.meta.UniqueFileId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents a video message (available in Telegram apps as of v.4.0)
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class VideoNote {

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
     * Video width and height (diameter of the video message) as defined by sender
     */
    private Integer length;

    /**
     * Duration of the audio in seconds as defined by sender
     */
    private Integer duration;

    /**
     * File size
     *
     * @optional
     */
    private @Optional
    Integer fileSize;


    /**
     * Thumbnail of the album cover to which the music file belongs
     *
     * @optional
     */
    private @Optional PhotoSize thumb;
}
