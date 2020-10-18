package com.goodboy.telegram.bot.core.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.method.message.SendMessageApi;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

public class Main {

    private final static ObjectMapper decoder = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        TelegramHttpClient client = HttpClientBuilder.defaultHttpClient();

        final String token = "1012816442:AAFUeiJqd2VU_XWAH9l5p50WvfNCn7r23Eg";

        TelegramCoreResponse<Message> message = client.send(new Request<>()
                .setToken(token)
                .setBody(new SendMessageApi()
                        .setChatId(422634112)
                        .setText("Im from Java code 2!")
                )
                .setCallName("sendMessage")
        );

        System.out.println(decoder.writeValueAsString(message));
    }
}
