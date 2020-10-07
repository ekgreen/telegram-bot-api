package com.goodboy.telegram.bot.api;

import java.util.List;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Contains information about documents or other Telegram Passport elements shared with the bot by the user
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class EncryptedPassportElement {

    /**
     * Element type. One of “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport”,
     * “address”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”,
     * “temporary_registration”, “phone_number”, “email”
     */
    private String type;

    /**
     * Base64-encoded encrypted Telegram Passport element data provided by the user, available for “personal_details”,
     * “passport”, “driver_license”, “identity_card”, “internal_passport” and “address” types.
     *
     * Can be decrypted and verified using the accompanying EncryptedCredentials
     *
     * @see EncryptedCredentials
     * @optional
     */
    private @Optional
    String data;

    /**
     * User's verified phone number, available only for “phone_number” type
     *
     * @optional
     */
    private @Optional String phoneNumber;

    /**
     * User's verified email address, available only for “email” type
     *
     * @optional
     */
    private @Optional String email;

    /**
     *  Array of encrypted files with documents provided by the user, available for “utility_bill”, “bank_statement”,
     *  “rental_agreement”, “passport_registration” and “temporary_registration” types.
     *
     *  Files can be decrypted and verified using the accompanying EncryptedCredentials
     *
     * @see EncryptedCredentials
     * @optional
     */
    private @Optional List<PassportFile> files;

    /**
     *  Encrypted file with the front side of the document, provided by the user. Available for “passport”,
     *  “driver_license”, “identity_card” and “internal_passport”.
     *
     *  The file can be decrypted and verified using the accompanying EncryptedCredentials
     *
     * @see EncryptedCredentials
     * @optional
     */
    private @Optional PassportFile frontSide;

    /**
     * Encrypted file with the reverse side of the document, provided by the user. Available for “driver_license” and
     * “identity_card”.
     *
     * The file can be decrypted and verified using the accompanying EncryptedCredentials
     *
     * @see EncryptedCredentials
     * @optional
     */
    private @Optional PassportFile reverseSide;

    /**
     * Encrypted file with the selfie of the user holding a document, provided by the user; available for “passport”,
     * “driver_license”, “identity_card” and “internal_passport”.
     *
     * The file can be decrypted and verified using the accompanying EncryptedCredentials
     *
     * @see EncryptedCredentials
     * @optional
     */
    private @Optional PassportFile selfie;

    /**
     * Array of encrypted files with translated versions of documents provided by the user. Available if requested for
     * “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”,
     * “rental_agreement”, “passport_registration” and “temporary_registration” types.
     *
     * Files can be decrypted and verified using the accompanying EncryptedCredentials
     *
     * @see EncryptedCredentials
     * @optional
     */
    private @Optional List<PassportFile> translation;

    /**
     * Base64-encoded element hash for using in PassportElementErrorUnspecified
     */
    private String hash;
}
