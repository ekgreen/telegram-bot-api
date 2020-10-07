package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object contains basic information about an invoice
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Invoice {

    /**
     * Product name
     */
    private String title;

    /**
     * Product description
     */
    private String description;

    /**
     * Unique bot deep-linking parameter that can be used to generate this invoice
     */
    private String startParameter;

    /**
     * Three-letter ISO 4217 currency code
     */
    private String currency;

    /**
     * Total price in the smallest units of the currency (integer, not float/double).
     * For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json,
     * it shows the number of digits past the decimal point for each currency (2 for the majority of currencies)
     */
    private Integer totalAmount;
}
