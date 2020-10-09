package com.goodboy.telegram.bot.api.client;

import java.net.http.HttpRequest;

public interface HttpBodyPublisher<V>  {

    HttpRequest.BodyPublisher instance(Object body);

    Class<V> publishingBodyType();
}
