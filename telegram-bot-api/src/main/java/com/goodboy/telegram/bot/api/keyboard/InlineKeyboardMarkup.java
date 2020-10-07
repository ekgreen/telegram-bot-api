package com.goodboy.telegram.bot.api.keyboard;

import java.util.List;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * This object represents an inline keyboard that appears right next to the message it belongs to
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class InlineKeyboardMarkup implements ReplyMarkup {

    /**
     * Array of button rows, each represented by an Array of InlineKeyboardButton objects
     */
    private List<List<InlineKeyboardButton>> inlineKeyboard;
}
