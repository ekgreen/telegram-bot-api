package com.goodboy.telegram.bot.spring.api.gateway;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.processor.BotMethodProcessorChain;

import java.lang.reflect.Method;

public interface GatewayFilterChain {

    public Object invoke(Object proxy, Method method, Object[] args, Update update);
}
