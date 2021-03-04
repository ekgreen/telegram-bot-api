package com.goodboy.telegram.example;

import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.methods.message.SendMessageApi;
import com.goodboy.telegram.bot.spring.api.meta.*;
import com.goodboy.telegram.example.service.BrownieCleaningService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

@Bot(value = "brownie", path = "/brownie")
public class Brownie {

    private @Autowired BrownieCleaningService service;

    @Webhook
    public @Nonnull Api onUpdate(
            @ChatId Integer chatId,
            @Nickname String nickname,
            @MessageText String text
    ) {
        return new SendMessageApi()
                .setChatId(chatId)
                .setText(service.sayHi(nickname, text));
    }

}
