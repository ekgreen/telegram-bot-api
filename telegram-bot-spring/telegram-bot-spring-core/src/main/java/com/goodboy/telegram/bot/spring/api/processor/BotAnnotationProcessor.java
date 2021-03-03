package com.goodboy.telegram.bot.spring.api.processor;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface BotAnnotationProcessor {

    @Nonnull BotMethodProcessorChain createAnnotationProcessor(@Nonnull Method method);

    Class<? extends Annotation> supports();
}
