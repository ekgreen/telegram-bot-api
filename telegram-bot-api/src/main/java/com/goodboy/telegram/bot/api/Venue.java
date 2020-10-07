package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

@TelegramApi
@Data
@Accessors(chain = true)
public class Venue {

    /**
     * Venue location
     */
    private Location location;

    /**
     * Name of the venue
     */
    private String title;

    /**
     * Address of the venue
     */
    private String address;

    /**
     * Foursquare identifier of the venue
     */
    private String foursquareId;

    /**
     * Foursquare type of the venue.
     * (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
     */
    private String foursquareType;
}
