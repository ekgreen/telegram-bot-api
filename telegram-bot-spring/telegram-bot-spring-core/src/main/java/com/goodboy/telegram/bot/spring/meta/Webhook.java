package com.goodboy.telegram.bot.spring.meta;

import com.goodboy.telegram.bot.api.Update;

import javax.annotation.Nonnull;

public interface Webhook {

    void onUpdate(@Nonnull Update update);
}
