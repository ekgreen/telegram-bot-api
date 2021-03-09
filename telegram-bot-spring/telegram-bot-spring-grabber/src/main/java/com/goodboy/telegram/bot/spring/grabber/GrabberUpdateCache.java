package com.goodboy.telegram.bot.spring.grabber;

import com.goodboy.telegram.bot.api.Update;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface GrabberUpdateCache {

    /**
     * save updates in cache or add in the back
     *
     * @param updates list of updates
     */
    void offer(List<Update> updates);

    /**
     * Get batch from cache
     *
      * @return list of updates or empty
     */
    @NotNull Batch peekBatch(int batchSize);

    /**
     * commiting batch in the cache
     *
     * @param uid unique batch id
     * @return
     */
    boolean commitBatch(@NotNull String uid);

    /**
     * @return true if cache is not empty
     */
    boolean isPresent();
}
