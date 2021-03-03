package com.goodboy.telegram.example;

import com.goodboy.telegram.bot.api.Chat;
import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.methods.message.SendMessageApi;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.spring.api.meta.*;
import com.goodboy.telegram.example.service.BrownieCleaningService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.List;

@Bot(value = "brownie", path = "/brownie")
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class Brownie {

    private @Autowired BrownieCleaningService service;

    @Webhook("/webhook")
    public @Nonnull SendMessageApi onUpdate(
            @ChatId Integer chatId,
            @Nickname String nickname,
            @MessageText String text
    ) {
        return new SendMessageApi()
                .setChatId(chatId)
                .setText(service.sayHi(nickname, text));
    }

    @Webhook("/test-webhook")
    public void onTestUpdate(
            @NotNull UpdateContext context,
            @ChatId Integer chatId,
            @NotNull Update update,
            @NotNull Chat chat,
            @NotNull Message message,
            @MessageText String text,
            @Nonnull User user,
            @Nickname String nickname
    ) {
        System.out.println(update);
        System.out.println(context);
        System.out.println(chatId);
        System.out.println(chat);
        System.out.println(message);
        System.out.println(text);
        System.out.println(user);
        System.out.println(nickname);
    }

//    @LongPolling
    public void longPolling(@NotNull List<Update> update) {
        System.out.println("boom!");
    }
}
