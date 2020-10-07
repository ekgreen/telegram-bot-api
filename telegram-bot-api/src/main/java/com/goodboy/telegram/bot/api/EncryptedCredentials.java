package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Contains data required for decrypting and authenticating {@link EncryptedPassportElement}.
 *
 *
 * See the Telegram Passport Documentation for a complete description of the data decryption and authentication processes
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
