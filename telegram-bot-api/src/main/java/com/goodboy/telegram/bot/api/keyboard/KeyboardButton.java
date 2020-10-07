package com.goodboy.telegram.bot.api.keyboard;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents one button of the reply keyboard.
 * For simple text buttons String can be used instead of this object
 * to specify text of the button. Optional fields request_contact, request_location,
 * and request_poll are mutually exclusive.
 *
 * Note: request_contact and request_location options will only work in Telegram versions
 * released after 9 April, 2016. Older clients will display unsupported message.
 *
 * Note: request_poll option will only work in Telegram versions released after
 * 23 January, 2020. Older clients will display unsupported message.
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class KeyboardButton {

    /**
     * Text of the button. If none of the optional fields are used, it will be sent
     * as a message when the button is pressed
     */
    private String text;

    /**
     * If True, the user's phone number will be sent as a contact when
     * the button is pressed. Available in private chats only
     *
     * @optional
     */
    private @Optional Boolean requestContact;

    /**
     * If True, the user's current location will be sent when the button is pressed.
     * Available in private chats only
     *
     * @optional
     */
    private @Optional Boolean requestLocation;

    /**
     * If specified, the user will be asked to create a poll and send it to
     * the bot when the button is pressed. Available in private chats only
     *
     * @optional
     */
    private @Optional KeyboardButtonPollType requestPoll;
}
