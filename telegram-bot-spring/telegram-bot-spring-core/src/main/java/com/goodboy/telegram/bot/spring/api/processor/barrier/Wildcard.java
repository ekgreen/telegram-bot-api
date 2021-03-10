package com.goodboy.telegram.bot.spring.api.processor.barrier;

import org.jetbrains.annotations.NotNull;

public interface Wildcard<T> {

    public T doWild(@NotNull T origin, @NotNull WildcardApiExecutor executor);
}
