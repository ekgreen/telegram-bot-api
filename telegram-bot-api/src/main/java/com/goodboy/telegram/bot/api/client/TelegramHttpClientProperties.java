package com.goodboy.telegram.bot.api.client;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TelegramHttpClientProperties {

    /**
     * Throw exception on non 200 response code
     */
    private boolean throwExceptionOnNonOkResponse = true;
}
