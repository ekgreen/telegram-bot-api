package com.goodboy.telegram.bot.spring.impl.processors.arguments;

import com.goodboy.telegram.bot.api.Chat;
import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.processor.arguments.TypeArgumentProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

@Infrastructure
public class ChatArgumentProcessor implements TypeArgumentProcessor<Chat> {

    @Override
    public void setArgument(int position, @Nonnull Class<?> argumentType, @NotNull Object[] arguments, @NotNull BotData botData, @Nullable Update update) {
        if (update != null) {
            arguments[position] = findChatFromUpdate(update);
        }
    }

    @Override
    public Class<? extends Chat> supportedType() {
        return Chat.class;
    }

    private @Nonnull Chat findChatFromUpdate(@Nonnull Update update) {
        return Stream.of(update.getMessage(), update.getEditedMessage(), update.getChannelPost(), update.getEditedChannelPost())
                .filter(Objects::nonNull)
                .findFirst()
                .map(Message::getChat)
                .orElseThrow(NullPointerException::new);
    }
}
