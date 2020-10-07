package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents a file uploaded to Telegram Passport.
 * Currently all Telegram Passport files are in JPEG format when decrypted and don't exceed 10MB
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class PassportFile {

    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    private String id;

    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots.
     * Can't be used to download or reuse the file
     */
    private String uniqueId;

    /**
     * File size
     */
    private Integer fileSize;

    /**
     * 	Unix time when the file was uploaded
     */
    private Integer fileData;
}
