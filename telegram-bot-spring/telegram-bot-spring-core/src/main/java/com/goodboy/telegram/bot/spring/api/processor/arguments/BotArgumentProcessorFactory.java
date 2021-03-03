package com.goodboy.telegram.bot.spring.api.processor.arguments;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

public interface BotArgumentProcessorFactory {

    @Nonnull BotArgumentProcessor createArgumentProcessor(@Nonnull Method method);
}
