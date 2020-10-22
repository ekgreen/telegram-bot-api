package com.goodboy.telegram.bot.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TelegramHttpResponse {

    /**
     * Returns the status code for this response.
     */
    int statusCode;

    /**
     * Returns the body;
     */
    byte[] body;
}
