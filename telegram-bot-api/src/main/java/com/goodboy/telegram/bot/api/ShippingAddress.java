package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents a shipping address
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class ShippingAddress {

    /**
     * ISO 3166-1 alpha-2 country code
     */
    private String countryCode;

    /**
     * 	State, if applicable
     */
    private String state;

    /**
     * City
     */
    private String city;

    /**
     * First line for the address
     */
    private String streetLine1;

    /**
     * Second line for the address
     */
    private String streetLine2;

    /**
     * Address post code
     */
    private String postCode;
}
