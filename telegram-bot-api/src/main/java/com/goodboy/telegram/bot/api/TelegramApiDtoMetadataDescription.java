package com.goodboy.telegram.bot.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramApiDtoMetadataDescription {

    /**
     * Unique message identifier inside this chat
     */
    public static final String MESSAGE_ID_DESCRIPTOR = "message_id";

    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    public static final String FILE_ID_DESCRIPTOR = "file_id";

    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots
     */
    public static final String UNIQUE_FILE_ID_DESCRIPTOR = "file_unique_id";
}
