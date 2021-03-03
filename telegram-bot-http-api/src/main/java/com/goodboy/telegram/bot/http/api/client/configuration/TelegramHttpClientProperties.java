package com.goodboy.telegram.bot.http.api.client.configuration;


import com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TelegramHttpClientProperties {

    /**
     * Throw exception on non 200 response code
     */
    private boolean throwExceptionOnNonOkResponse = true;

    /**
     * Desire http method on controversial situations
     */
    private HttpMethod desireHttpMethod = HttpMethod.POST;
}
