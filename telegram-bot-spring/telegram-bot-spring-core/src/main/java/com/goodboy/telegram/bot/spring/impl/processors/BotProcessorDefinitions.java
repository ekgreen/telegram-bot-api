package com.goodboy.telegram.bot.spring.impl.processors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BotProcessorDefinitions {

    // Приоритет [thread_local] для тред локального процессора контекстов
    public static final int THREAD_LOCAL_PRIORITY = -100_000;

    // Приоритет [argument_local] для аргумент локального процессора контекстов
    public static final int ARGUMENT_LOCAL_PRIORITY = -50_000;
}
