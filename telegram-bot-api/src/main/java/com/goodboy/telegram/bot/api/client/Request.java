package com.goodboy.telegram.bot.api.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Request<T> {

    private String authToken;

    private String callName;

    private String host;

    private String path;

    private T body;
}
