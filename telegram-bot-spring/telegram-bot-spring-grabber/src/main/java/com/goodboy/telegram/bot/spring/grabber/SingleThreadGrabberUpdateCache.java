package com.goodboy.telegram.bot.spring.grabber;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiRuntimeException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SingleThreadGrabberUpdateCache implements GrabberUpdateCache {

    private final static int DEFAULT_CACHE_STRENGTH = 3;

    // how many times update could be saved
    private final int cacheStrength; // todo

    // cached batch
    private Batch cached;
    // ready for peek - all changes commited
    private boolean flag = true;

    public SingleThreadGrabberUpdateCache(int cacheStrength) {
        this.cacheStrength = cacheStrength;
    }

    public SingleThreadGrabberUpdateCache() {
        this(DEFAULT_CACHE_STRENGTH);
    }

    public void offer(List<Update> updates) {
        if (!flag)
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.VALIDATION_EXCEPTION, "attempt to offer batch before commit previous");

        cached = new Batch(updates);
    }

    public @NotNull Batch peekBatch(int batchSize) {
        // ignore parameter - coz batchSize to cache request cannot be more than saved batch in cache
        flag = false;
        return cached;
    }

    public boolean commitBatch(@NotNull String uid) {
        if (cached == null)
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.VALIDATION_EXCEPTION, "attempt to commit batch before offer");

        if (!uid.equals(cached.getUid()))
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.VALIDATION_EXCEPTION, "not sequential commit");

        return flag = true;
    }

    public boolean isPresent() {
        return false;
    }
}
