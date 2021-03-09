/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This object contains information about an incoming pre-checkout query
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class PreCheckoutQuery {

    /**
     * Unique query identifier
     */
    private String id;

    /**
     * User who sent the query
     */
    private User from;

    /**
     * Three-letter ISO 4217 currency code
     */
    private String currency;

    /**
     * Total price in the smallest units of the currency (integer, not float/double). For example, for a price of
     * US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past
     * the decimal point for each currency (2 for the majority of currencies).
     */
    private Long totalAmount;

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
    private OrderInfo orderInfo;
}
