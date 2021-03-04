package com.goodboy.telegram.bot.spring.impl.processors.arguments;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.meta.Nickname;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.processor.arguments.TypeArgumentProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.Stream;

@Infrastructure
public class UserNicknameArgumentProcessor implements TypeArgumentProcessor<Nickname> {

    @Override
    public void setArgument(int position, @Nonnull Class<?> argumentType, @NotNull Object[] arguments, @NotNull BotData botData, @Nullable Update update) {
        if (update != null) {
            final User from = findUserFromUpdate(update);

            arguments[position] = from.getUsername();
        }
    }

    @Override
    public Class<? extends Nickname> supportedType() {
        return Nickname.class;
    }

    private @Nonnull User findUserFromUpdate(@Nonnull Update update) {
        return Stream.of(update.getMessage(), update.getEditedMessage(), update.getChannelPost(), update.getEditedChannelPost())
                .filter(Objects::nonNull)
                .findFirst()
                .map(Message::getFrom)
                .orElseThrow(NullPointerException::new);
    }
}
