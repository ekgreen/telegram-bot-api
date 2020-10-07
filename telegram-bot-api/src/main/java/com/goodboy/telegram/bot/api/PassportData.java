package com.goodboy.telegram.bot.api;

import java.util.List;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Contains information about Telegram Passport data shared with the bot by the user
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
