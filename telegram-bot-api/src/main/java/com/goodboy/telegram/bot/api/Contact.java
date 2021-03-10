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
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents a phone contact
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class Contact {

    /**
     * 	Contact's phone number
     */
    private String phoneNumber;

    /**
     * Contact's first name
     */
    private String firstName;

    /**
     * Contact's last name
     *
     * @optional
     */
    private @Optional
    String lastName;

    /**
     * Contact's user identifier in Telegram
     *
     * @optional
     */
    private Long userId;

    /**
     * Additional data about the contact in the form of a vCard
     *
     * @optional
     */
    private String vcard;
}
