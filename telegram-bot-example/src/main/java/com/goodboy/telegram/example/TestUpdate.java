package com.goodboy.telegram.example;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.method.message.TelegramMessageApi;
import com.goodboy.telegram.bot.spring.api.bot.Bot;
import com.goodboy.telegram.bot.spring.api.meta.WebhookApi;
import com.goodboy.telegram.bot.spring.api.handlers.token.Token;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

@WebhookApi(bot = "test")
@RequiredArgsConstructor(onConstructor_=@Autowired)
public class TestUpdate implements Bot {

    private @com.goodboy.telegram.bot.spring.api.meta.Bot
    Token token;

    private final TelegramMessageApi messageApi;

    @Override
    public void onUpdate(@NotNull Update update) {
        messageApi.sendMessage(update.getMessage().getChat().getId(), String.valueOf(Math.random()));
    }
}
