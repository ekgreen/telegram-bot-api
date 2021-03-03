package com.goodboy.telegram.bot.spring.api.processor;

import java.lang.reflect.Method;

public interface BotMethodProcessorChain {

    public Object invoke(Object proxy, Method method, Object[] args);
}
