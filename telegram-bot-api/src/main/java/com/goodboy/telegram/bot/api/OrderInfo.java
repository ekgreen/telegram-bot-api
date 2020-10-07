package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents information about an order
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class OrderInfo {

    /**
     * User name
     *
     * @optional
     */
    private @Optional
    String name;

    /**
     * User's phone number
     *
     * @optional
     */
    private @Optional String phoneNumber;

    /**
     * User email
     *
     * @optional
     */
    private @Optional String email;

    /**
     * User shipping address
     *
     * @optional
     */
    private @Optional ShippingAddress shippingAddress;
}
