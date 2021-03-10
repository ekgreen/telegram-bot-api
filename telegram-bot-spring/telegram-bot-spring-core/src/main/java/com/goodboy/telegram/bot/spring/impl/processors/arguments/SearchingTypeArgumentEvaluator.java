package com.goodboy.telegram.bot.spring.impl.processors.arguments;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.processor.arguments.TypeArgumentEvaluator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Stream;

@Infrastructure
public class SearchingTypeArgumentEvaluator implements TypeArgumentEvaluator {

    /**
     * Search chat id through update
     * <p>
     * todo make full research
     *
     * @param update request object
     * @return chat id - have to be present [all messages received from chat's, or not]
     */
    public @NotNull String getChatId(@NotNull Update update) {
        return Stream.of(update.getMessage(), update.getEditedMessage(), update.getChannelPost(), update.getEditedChannelPost())
                .filter(Objects::nonNull)
                .findFirst()
                .map(message -> String.valueOf(message.getChat().getId()))
                .orElse("");
    }
}
