package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents a phone contact
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
    private @Optional Integer userId;

    /**
     * Additional data about the contact in the form of a vCard
     *
     * @optional
     */
    private @Optional String vcard;
}
