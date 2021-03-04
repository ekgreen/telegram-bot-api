package com.goodboy.telegram.bot.api;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * This object represents a point on the map
 */
@TelegramApi
@Data
@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
public class Location {

    /**
     * Longitude as defined by sender
     */
    private Double longitude;

    /**
     * Latitude as defined by sender
     */
    private Double latitude;
}
