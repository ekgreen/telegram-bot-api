package com.goodboy.telegram.bot.example;

import com.goodboy.telegram.bot.api.Chat;
import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.spring.api.meta.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

@Slf4j
@Bot(value = "example", path = "/example")
public class ShowExampleBot {

    @Webhook(command = "show")
    public void showUpdate(
            @NotNull UpdateContext context,
            @ChatId Integer chatId,
            @NotNull Update update,
            @NotNull Chat chat,
            @NotNull Message message,
            @MessageText String text,
            @Nonnull User user,
            @Nickname String nickname
    ) {
        log.info(
                "\nupdate = {}\ncontext = {}\nchart_id = {}\nchat = {}\nmessage = {}\ntext = {}\nuser = {}\nnickname = {}",
                update, context, chatId, chat, message, text, user, nickname
        );
    }

    @Webhook(command = "salute")
    public void showNickname(
            @Nickname String nickname
    ) {
        log.info("nickname = {}", nickname);
    }

    @Webhook(command = {"show", "salute", "word"})
    public void showChatId(
            @ChatId Integer chatId
    ) {
        log.info("chat id = {}", chatId);
    }

    @Webhook
    public void showMessageText(
            @MessageText String text
    ) {
        log.info("message text = {}", text);
    }
}
