package com.goodboy.telegram.bot.spring.api.gateway;

import com.goodboy.telegram.bot.api.Update;

import java.lang.reflect.Method;

public interface GatewayFilter {

    public Object invoke(Object proxy, Method method, Object[] args, Update update, GatewayFilterChain chain);
}
