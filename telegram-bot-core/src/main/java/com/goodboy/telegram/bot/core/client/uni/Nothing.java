package com.goodboy.telegram.bot.core.client.uni;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Nothing {
    private final static Nothing INSTANCE = new Nothing();

    public static Nothing create() {
        return INSTANCE;
    }
}
