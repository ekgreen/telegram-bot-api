package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.FileId;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.meta.UniqueFileId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents an animation file (GIF or H.264/MPEG-4 AVC video without sound)
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Animation{

    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    @FileId
    private String id;

    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
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
     * Duration of the video in seconds as defined by sender
     */
    private Integer duration;

    /**
     * Animation thumbnail as defined by sender
     *
     * @optional
     */
    private @Optional
    PhotoSize thumb;

    /**
     * Original animation filename as defined by sender
     *
     * @optional
     */
    private @Optional String fileName;

    /**
     *  MIME type of the file as defined by sender
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

}
