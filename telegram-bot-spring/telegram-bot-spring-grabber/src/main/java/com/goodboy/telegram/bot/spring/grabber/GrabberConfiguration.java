package com.goodboy.telegram.bot.spring.grabber;


import com.goodboy.telegram.bot.api.methods.updates.UpdateType;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GrabberConfiguration {

    /**
     * Grabber bot name
     */
    private final String botName;

    /**
     * Token resolver for bot requests
     */
    private final TelegramApiTokenResolver tokenResolver;

    /**
     * Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     *
     * @optional
     */
    private Integer limit;

    /**
     * Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive, short polling
     * should be used for testing purposes only.
     *
     * @optional
     */
    private Long timeout;

    /**
     * A JSON-serialized list of the update types you want your bot to receive. For example, specify [“message”,
     * “edited_channel_post”, “callback_query”] to only receive updates of these types. See Update for a complete list
     * of available update types. Specify an empty list to receive all updates regardless of type (default). If not
     * specified, the previous setting will be used.
     * <p>
     * Please note that this parameter doesn't affect updates created before the call to the getUpdates, so unwanted
     * updates may be received for a short period of time.
     *
     * @optional
     */
    private List<UpdateType> allowedUpdates;

    /**
     * commit partly executed batch
     *
     * it means that bot could (available) supplement partly executed batch with new batch (of smaller size)
     */
    private boolean commitPartlyExecuted = true;

    /**
     * cache partly executed
     *
     * it means that bot could (available) cache partly executed request
     */
    private boolean cachePartlyExecuted = true;
}
