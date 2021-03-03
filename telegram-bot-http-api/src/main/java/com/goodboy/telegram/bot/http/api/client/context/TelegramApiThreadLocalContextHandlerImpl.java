package com.goodboy.telegram.bot.http.api.client.context;

import org.jetbrains.annotations.NotNull;

public class TelegramApiThreadLocalContextHandlerImpl implements TelegramApiContextHandler {

    private final static ThreadLocal<UpdateContext> THREAD_CACHE = new ThreadLocal<>();

    public UpdateContext read() {
        return THREAD_CACHE.get();
    }

    public void create(@NotNull UpdateContext context) {
        THREAD_CACHE.set(context);
    }

    public void delete() {
        THREAD_CACHE.set(null);
    }
}
