package com.goodboy.telegram.bot.api.response;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

@TelegramApi
@Data
@Accessors(chain = true)
public class TelegramCoreResponse<T> {

    /**
     * Success sign
     */
    private Boolean ok;

    /**
     * Error code
     *
     * @optional
     */
    private @Optional Integer errorCode;

    /**
     * Description of request action
     *
     * @optional
     */
    private @Optional String description;

    /**
     * Additional response parameters
     *
     * @optional
     */
    private @Optional ResponseParameters parameters;

    /**
     * Body
     *
     * @optional
     */
    private @Optional T result;

}
