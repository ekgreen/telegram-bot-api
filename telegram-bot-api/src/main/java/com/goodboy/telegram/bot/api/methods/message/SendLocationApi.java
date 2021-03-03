package com.goodboy.telegram.bot.api.methods.message;

import com.goodboy.telegram.bot.api.keyboard.ReplyMarkup;
import com.goodboy.telegram.bot.api.meta.Multipart;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Data
@Multipart
@TelegramApi
@Accessors(chain = true)
public class SendLocationApi implements Api {

    /**
     * Unique identifier for the target chat or username
     * of the target channel (in the format @channelusername)
     */
    private Integer  chatId;

    /**
     * Latitude of the location
     */
    private Double latitude;

    /**
     * Longitude of the location
     */
    private Double longitude;

    /**
     * Period in seconds for which the location will be updated
     * (see Live Locations, should be between 60 and 86400.
     *
     * @see <a href="https://telegram.org/blog/live-locations">Live Locations</a>
     */
    private Integer livePeriod;

    /**
     * Sends the message silently. Users will receive a notification with no sound
     *
     * @optional
     */
    private @Optional Boolean disableNotification;

    /**
     * If the message is a reply, ID of the original message
     *
     * @optional
     */
    private @Optional Integer replyToMessageId;

    /**
     * Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     * instructions to remove reply keyboard or to force a reply from the user.
     *
     * @optional
     */
    private @Optional ReplyMarkup replyMarkup;
}
