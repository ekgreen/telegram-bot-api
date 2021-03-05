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

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Contains data required for decrypting and authenticating {@link EncryptedPassportElement}.
 *
 * See the Telegram Passport Documentation for a complete description of the data decryption and authentication processes
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class EncryptedCredentials {

    /**
     * 	Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes and secrets required
     * 	for EncryptedPassportElement decryption and authentication
     */
    private String data;

    /**
     * Base64-encoded data hash for data authentication
     */
    private String hash;

    /**
     * Base64-encoded secret, encrypted with the bot's @TelegramApi
@Data
@Accessors(chain = true)
public RSA key, required for data decryption
     */
    private String secret;
}
