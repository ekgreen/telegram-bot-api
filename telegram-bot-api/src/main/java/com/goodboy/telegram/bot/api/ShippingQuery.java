package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object contains information about an incoming shipping query
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class ShippingQuery {

    /**
     * Unique query identifier
     */
    private String id;

    /**
     * User who sent the query
     */
    private User from;

    /**
     * Bot specified invoice payload
     */
    private String invoicePayload;

    /**
     * User specified shipping address
     */
    private ShippingAddress shippingAddress;
}
