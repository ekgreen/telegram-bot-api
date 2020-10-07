package com.goodboy.telegram.bot.api.response;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

@TelegramApi
@Data
@Accessors(chain = true)
public class ResponseParameters {

    /**
     * The group has been migrated to a supergroup with the specified identifier.
     * This number may be greater than 32 bits and some programming languages may have
     * difficulty/silent defects in interpreting it. But it is smaller than 52 bits,
     * so a signed 64 bit integer or double-precision float type are safe for storing this identifier
     *
     * @optional
     */
    private @Optional Integer migrateToChatId;

    /**
     * In case of exceeding flood control a number of seconds to
     * wait before the request can be repeated
     *
     * @optional
     */
    private @Optional Integer retryAfter;
}
