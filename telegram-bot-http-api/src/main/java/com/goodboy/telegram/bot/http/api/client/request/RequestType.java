package com.goodboy.telegram.bot.http.api.client.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.goodboy.telegram.bot.http.api.client.configuration.TelegramHttpClientDefinition.CAll_METHOD_COMMAND_PATH;
import static com.goodboy.telegram.bot.http.api.client.configuration.TelegramHttpClientDefinition.CAll_METHOD_FILE_PATH;

@Getter
@RequiredArgsConstructor
public enum RequestType {
    FILE(CAll_METHOD_FILE_PATH),
    COMMAND(CAll_METHOD_COMMAND_PATH)
    ;

    private final String path;

}
