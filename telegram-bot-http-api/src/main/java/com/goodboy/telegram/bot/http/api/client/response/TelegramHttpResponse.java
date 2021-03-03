package com.goodboy.telegram.bot.http.api.client.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Условно низкоуровневый формат ответа от API Телеграм - условно, потому что данный ответ является оберткой над
 * данными HTTP-ответа, например код ответа и набором байт из которого состоит тело сообщения
 */
@Data
@Accessors(chain = true)
public class TelegramHttpResponse {

    /**
     * Returns the status code for this response.
     */
    int statusCode;

    /**
     * Returns the body;
     */
    byte[] body;
}
