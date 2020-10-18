package com.goodboy.telegram.bot.core.client.uni;

import com.goodboy.telegram.bot.api.client.Request;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UniTypeRequest<V> {

    private Request<V> request;

    // url for sending requests
    private String url;

}
