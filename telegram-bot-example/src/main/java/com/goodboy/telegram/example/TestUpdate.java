package com.goodboy.telegram.example;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.meta.Webhook;
import com.goodboy.telegram.bot.spring.meta.WebhookApi;
import org.jetbrains.annotations.NotNull;

@WebhookApi(bot = "test")
public class TestUpdate implements Webhook {

    @Override
    public void onUpdate(@NotNull Update update) {
        System.out.println(update);
    }
}
