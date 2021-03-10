package com.goodboy.telegram.bot.spring.api.processor.barrier;

import org.jetbrains.annotations.NotNull;

public interface OriginChainBarrier {

    /**
     * Return False if return type have to be suspend in origin chain, in other words
     * origin chain protects return value and keep it inside
     *
     * @param returnType return value of evaluation method
     * @return true\false
     */
    public boolean isNotUnderArmor(@NotNull Class<?> returnType);
}
