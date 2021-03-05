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

import java.util.List;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Contains information about Telegram Passport data shared with the bot by the user
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class PassportData {

    /**
     * Array with information about documents and other Telegram Passport elements that was shared with the bot
     */
    private List<EncryptedPassportElement> data;

    /**
     * Encrypted credentials required to decrypt the data
     */
    private EncryptedCredentials credentials;
}
