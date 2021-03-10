package com.goodboy.telegram.bot.spring.grabber;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.methods.ApiRequest;
import com.goodboy.telegram.bot.api.methods.updates.GetUpdateApi;
import com.goodboy.telegram.bot.api.methods.updates.TelegramGetUpdatesApi;
import com.goodboy.telegram.bot.api.methods.updates.Updates;
import com.goodboy.telegram.bot.spring.api.gateway.Gateway;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayBatchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TelegramBotGrabber {

    /**
     * bot name - using for logging
     */
    private final String botName;

    /**
     * Telegram api which polling updates
     */
    private final TelegramGetUpdatesApi getUpdatesApi;

    /**
     * Gateway for routing requests
     */
    private final Gateway gateway;

    /**
     * Executor configuration
     */
    private final GrabberConfiguration configuration;

    /**
     * Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of
     * previously received updates. By default, updates starting with the earliest unconfirmed update are returned.
     * An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id.
     * The negative offset can be specified to retrieve updates starting from -offset update from the end of the updates
     * queue. All previous updates will forgotten.
     * <p>
     * Initial value for every executor is 0
     */
    private long offset = 0;

    /**
     * grabber update cache needs for save not executed updates and re-attempt them
     */
    private final GrabberUpdateCache cache;

    /**
     * grab updates and execute them
     */
    public void grabAndExecuteUpdates() {
        // allocate list for batch
        final List<Update> batch = new ArrayList<>();
        int batchSize = configuration.getLimit();
        int polledBatchSize = 0;
        String batchUid = null;

        // used multiple times
        final int estimatedBatchSize = configuration.getLimit();

        // check cache - if save available and cache not empty

        if (configuration.isCachePartlyExecuted() && cache.isPresent()) {
            final @NotNull Batch updates = cache.peekBatch(estimatedBatchSize);

            // add cached updates to batch
            if (updates.batchSize() > 0) {
                batchUid = updates.getUid();
                batch.addAll(updates.getElements());
            }
        }

        // process
        final int preProcessBatchSize = batch.size();
        // if batch size not 0 -> means that it was cache hit;
        if (preProcessBatchSize > 0)
            // if we able to commit failed batches - it means we could addition batch by new request data
            batchSize = configuration.isCommitPartlyExecuted() ? estimatedBatchSize - preProcessBatchSize : 0;

        // if batch size more than 0 make poll
        if (batchSize > 0) {
            // grabbed updates from telegram
            final Updates updates = getUpdatesApi.getUpdates(new ApiRequest<GetUpdateApi>()
                    .setApi(new GetUpdateApi()
                            .setOffset(offset)
                            .setLimit(batchSize)
                            .setTimeout(configuration.getTimeout())
                            .setAllowedUpdates(configuration.getAllowedUpdates())
                    )
                    .setToken(configuration.getTokenResolver().get())
            )
                    .optional()
                    .orElseGet(Updates::new);

            if ((polledBatchSize = updates.size()) > 0)
                batch.addAll(updates);
        }

        // recalculating batch size
        final int postProcessBatchSize = batch.size();

        // nothing to execute
        if (postProcessBatchSize == 0)
            return;

        if (log.isDebugEnabled())
            log.debug("[grabber] created bots {{}} routing request to gateway with { batch_size = {} where cached = {} and polled = {} }",
                    botName, postProcessBatchSize, preProcessBatchSize, batchSize
            );

        // routing request
        // routing succeed (get any result from gateway) commit changes
        final GatewayBatchResponse routingResponse = gateway.routing(configuration.getBotName(), batch);

        final boolean isOk = routingResponse.isOk();

        if (log.isDebugEnabled())
            log.debug("[grabber] bots {{}} routing request {} executed { ok = {}, fail = {} }",
                    botName,
                    isOk ? "succeed" : "partly",
                    isOk ? postProcessBatchSize : postProcessBatchSize - routingResponse.failedCount(),
                    isOk ? 0 : routingResponse.failedCount()
            );

        // commit cache
        if (batchUid != null)
            cache.commitBatch(batchUid);

        // commit changes

        // on success just commit offset
        if (isOk) {
            offset = batch.get(postProcessBatchSize - 1).getUpdateId() + 1;
            return;
        }

        // on fail ...
        routingResponse.onFail().ifPresent(failed -> {
            int failedCount = 0;

            // cache failed
            if (configuration.isCachePartlyExecuted()) {
                final List<Update> updatesToCache = failed
                        .stream()
                        .filter(fail -> fail.getCode() == 429) // only 429 code could be reattached
                        .map(GatewayBatchResponse.Fail::getUpdate)
                        .collect(Collectors.toList());

                failedCount = updatesToCache.size();
            }

            // commit failed
            if (failedCount == 0 || configuration.isCommitPartlyExecuted())
                offset = batch.get(postProcessBatchSize - 1).getUpdateId() + 1;
        });
    }
}
