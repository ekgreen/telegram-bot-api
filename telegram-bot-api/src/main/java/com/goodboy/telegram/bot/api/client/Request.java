package com.goodboy.telegram.bot.api.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Request<V> {

    private String token;

    private String callName;

    private String host;

    private String path;

    private Class<?> responseType;

    private V body;
}
