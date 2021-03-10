package com.goodboy.telegram.bot.spring.api.processor.barrier;

import com.goodboy.telegram.bot.spring.api.data.BotData;

import javax.annotation.Nonnull;

public interface OriginChainWildcard<T> {

    public Class<?> wildcardType();

    public Wildcard<T> createWildcard(
            @Nonnull BotData botData,
            @Nonnull Class<?> returnType
    );
}
