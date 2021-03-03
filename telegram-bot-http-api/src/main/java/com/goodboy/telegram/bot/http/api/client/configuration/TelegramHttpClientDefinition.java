package com.goodboy.telegram.bot.http.api.client.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramHttpClientDefinition {

    public static final String CAll_METHOD_COMMAND_PATH = "";
    public static final String CAll_METHOD_FILE_PATH = "/file";

    /**
     * Default endpoint is <a href="https://api.telegram.org/">telegram</a>
     * Will replaced by null host value
     */
    public static final String DEFAULT_HOST = "https://api.telegram.org";
}
