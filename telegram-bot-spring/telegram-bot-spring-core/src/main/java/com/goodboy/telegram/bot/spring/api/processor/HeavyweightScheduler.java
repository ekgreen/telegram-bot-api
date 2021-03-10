package com.goodboy.telegram.bot.spring.api.processor;

import com.goodboy.telegram.bot.spring.api.actions.FutureFastAction;
import org.jetbrains.annotations.NotNull;

public interface HeavyweightScheduler {

    /**
     *
     *
     * @param action
     */
    public void poll(@NotNull FutureFastAction action);
}
