package com.goodboy.telegram.bot.api.client;

import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

public interface PathHandler<T> {

    TelegramCoreResponse<T> handle(byte[] response);

    String handlingOn();
}
