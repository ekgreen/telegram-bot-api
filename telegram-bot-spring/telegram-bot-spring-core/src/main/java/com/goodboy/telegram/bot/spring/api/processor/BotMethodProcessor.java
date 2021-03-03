package com.goodboy.telegram.bot.spring.api.processor;

import org.springframework.core.Ordered;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

public interface BotMethodProcessor extends Ordered {

    /**
     * Useful constant for the inert precedence value.
     * @see java.lang.Integer#MAX_VALUE
     */
    public static final int INERT_PRECEDENCE = Integer.MAX_VALUE / 2;

    public Object invoke(Object proxy, Method method, Object[] args, BotMethodProcessorChain chain);


    public default boolean supports(@Nonnull Method method) { return true; };

    /**
     * Handle ONLY integer positive numbers, negative reserved for platform technic processors
     *
     * @return preferable process order, or 0 if order value is negative
     */
    public default int getOrder() { return INERT_PRECEDENCE; };
}
