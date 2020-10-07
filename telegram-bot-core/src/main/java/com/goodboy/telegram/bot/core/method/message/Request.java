package com.goodboy.telegram.bot.core.method.message;

public class Request<T> {

    private String authToken;

    private Service service;

    private T body;
}
