package com.goodboy.telegram.bot.spring.api.processor;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 *
 */
public interface BotAnnotationProcessorFactory {

    /**
     *
     * @param method
     * @return
     */
    @Nonnull BotMethodProcessorChain createAnnotationProcessor(@Nonnull Method method);

    /**
     *
     * @param method
     * @return
     */
    default boolean supports(Method method) {return true; }
}
