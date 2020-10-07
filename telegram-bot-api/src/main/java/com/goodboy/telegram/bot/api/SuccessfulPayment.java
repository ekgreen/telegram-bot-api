package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object contains basic information about a successful payment
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class SuccessfulPayment {

    /**
     * Three-letter ISO 4217 currency code
     */
    private String currency;


    /**
     * Total price in the smallest units of the currency (integer, not float/double).
     * For example, for a price of US$ 1.45 pass amount = 145.
     * See the exp parameter in currencies.json, it shows the number of digits past the decimal point
     * for each currency (2 for the majority of currencies).
     */
    private Integer totalAmount;


    /**
     * Bot specified invoice payload
     */
    private String invoicePayload;

    /**
     * Identifier of the shipping option chosen by the user
     *
     * @optional
     */
    private @Optional
    String shippingOptionId;

    /**
     * Order info provided by the user
     *
     * @optional
     */
    private @Optional OrderInfo orderInfo;


    /**
     * Telegram payment identifier
     */
    private String telegramPaymentChargeId;


    /**
     * Provider payment identifier
     */
    private String providerPaymentChargeId;
}
