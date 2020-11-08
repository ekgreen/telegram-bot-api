package com.goodboy.telegram.bot.api.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Request<V> {

    private String token;

    private String callName;

    private String path;

    private RequestType requestType = RequestType.AUTO;

    private Class<?> responseType;

    private V body;

    public enum RequestType{
        AUTO, POST, GET, MULTIPART
    }
}
