package com.goodboy.telegram.bot.spring.impl.processors.arguments;

import com.goodboy.telegram.bot.api.Chat;
import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiUpdateContextImpl;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.spring.api.meta.ChatId;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.processor.BotData;
import com.goodboy.telegram.bot.spring.api.processor.arguments.TypeArgumentProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Infrastructure
public class ChatIdArgumentProcessor implements TypeArgumentProcessor<ChatId> {

    @Override
    public void setArgument(int position, @Nonnull Class<?> argumentType, @NotNull Object[] arguments, @NotNull BotData botData, @Nullable Update update) {
        if (update != null) {
            final Chat chat = findChatFromUpdate(update);
            arguments[position] = chat.getId();
        }
    }

    @Override
    public Class<? extends ChatId> supportedType() {
        return ChatId.class;
    }

    private @Nonnull Chat findChatFromUpdate(@Nonnull Update update) {
        return Stream.of(update.getMessage(), update.getEditedMessage(), update.getChannelPost(), update.getEditedChannelPost())
                .filter(Objects::nonNull)
                .findFirst()
                .map(Message::getChat)
                .orElseThrow(NullPointerException::new);
    }
}
