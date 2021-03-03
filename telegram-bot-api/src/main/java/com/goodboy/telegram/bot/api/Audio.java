package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.FileId;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.meta.UniqueFileId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents an audio file to be treated as music by the Telegram clients
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Audio {

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
     * Duration of the audio in seconds as defined by sender
     */
    private Integer duration;

    /**
     * Performer of the audio as defined by sender or by audio tags
     *
     * @optional
     */
    private @Optional String performer;


    /**
     * Title of the audio as defined by sender or by audio tags
     *
     * @optional
     */
    private @Optional String title;


    /**
     * MIME type of the file as defined by sender
     *
     * @optional
     */
    private @Optional String mimeType;


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
