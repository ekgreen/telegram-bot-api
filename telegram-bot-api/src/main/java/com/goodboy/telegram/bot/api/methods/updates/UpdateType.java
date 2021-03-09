package com.goodboy.telegram.bot.api.methods.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goodboy.telegram.bot.api.meta.TelegramApi;

@TelegramApi
public enum UpdateType {
    @JsonProperty("message")
    MESSAGE,
    @JsonProperty("edited_channel_post")
    EDITED_CHANNEL_POST,
    @JsonProperty("callback_query")
    CALLBACK_QUERY
    ;
}
