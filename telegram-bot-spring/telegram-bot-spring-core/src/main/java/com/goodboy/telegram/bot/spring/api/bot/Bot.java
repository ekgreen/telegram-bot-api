package com.goodboy.telegram.bot.spring.api.bot;

import com.goodboy.telegram.bot.api.Update;

import org.jetbrains.annotations.NotNull;

public interface Bot {

    void onUpdate(@NotNull Update update);
}
