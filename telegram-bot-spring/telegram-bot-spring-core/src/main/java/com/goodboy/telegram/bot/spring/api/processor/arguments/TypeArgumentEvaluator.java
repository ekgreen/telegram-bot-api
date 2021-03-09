package com.goodboy.telegram.bot.spring.api.processor.arguments;

import com.goodboy.telegram.bot.api.Update;
import org.jetbrains.annotations.NotNull;

public interface TypeArgumentEvaluator {

    @NotNull String getChatId(@NotNull Update update);
}
