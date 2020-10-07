package com.goodboy.telegram.bot.api.method.message;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

import javax.annotation.Nonnull;
import java.io.IOException;


public interface TelegramMessageApi {

    /**
     * Use this method to send text messages
     *
     * @return on success, the sent Message is returned.
     */
    @Nonnull TelegramCoreResponse<Message> sendMessage(@Nonnull SendMessageApi request) throws IOException, InterruptedException;

    /**
     * Double-argument method includes required parameter
     *
     * @param chatId unique chat identifier
     * @param text   text of the message to be sent, 1-4096 characters after entities parsing
     *
     * @see TelegramMessageApi#sendMessage(SendMessageApi)
     */
    default @Nonnull TelegramCoreResponse<Message> sendMessage(@Nonnull Integer chatId, @Nonnull String text) {
        return sendMessage(new SendMessageApi()
                .setChatId(chatId)
                .setText(text)
        );
    }


}
