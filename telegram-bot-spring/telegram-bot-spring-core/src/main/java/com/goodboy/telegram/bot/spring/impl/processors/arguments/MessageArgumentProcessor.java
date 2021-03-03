package com.goodboy.telegram.bot.spring.impl.processors.arguments;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.processor.BotData;
import com.goodboy.telegram.bot.spring.api.processor.arguments.TypeArgumentProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

@Infrastructure
public class MessageArgumentProcessor implements TypeArgumentProcessor<Message> {

    @Override
    public void setArgument(int position, @Nonnull Class<?> argumentType, @NotNull Object[] arguments, @NotNull BotData botData, @Nullable Update update) {
        if (update != null) {
            arguments[position] = findMessageFromUpdate(update);
        }
    }

    @Override
    public Class<? extends Message> supportedType() {
        return Message.class;
    }

    private @Nonnull Message findMessageFromUpdate(@Nonnull Update update) {
        return Stream.of(update.getMessage(), update.getEditedMessage(), update.getChannelPost(), update.getEditedChannelPost())
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(NullPointerException::new);
    }
}
