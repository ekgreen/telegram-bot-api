package com.goodboy.telegram.bot.core.client;

import java.net.http.HttpRequest;

public interface HttpBodyPublisher<V>  {

    HttpRequest.BodyPublisher instance(Object body);

    Class<V> publishingBodyType();
}
