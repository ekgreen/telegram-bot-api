package com.goodboy.telegram.bot.spring.processors.arguments;


import com.goodboy.telegram.bot.api.Chat;
import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.impl.processors.arguments.ChatArgumentProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

class ChatArgumentProcessorTest {

    private final static BotData BOT_DATA = new BotData(
            "any",
            new String[]{"/any"},
            () -> null
    );

    @ParameterizedTest
    @MethodSource("chatUpdateSource")
    void setArgument(Integer expected, Update update) {
        // 1. create processor
        final var processor = new ChatArgumentProcessor();
        // 2. create object array
        final var args = new Object[1];
        // 3. execute processor
        processor.setArgument(0, Chat.class, args, BOT_DATA, update);

        Assertions.assertEquals(expected, args[0] != null ? ((Chat) args[0]).getId() : null);
    }

    @Test
    void supportedType() {
        // 1. create processor
        final var processor = new ChatArgumentProcessor();

        Assertions.assertEquals(Chat.class, processor.supportedType());
    }

    static Stream<Arguments> chatUpdateSource() {
        return Stream.of(
                Arguments.of(null,null),
                Arguments.of(
                        5,
                        new Update()
                                .setMessage(new Message()
                                        .setChat(new Chat()
                                                .setId(5)
                                        )
                                )
                ),
                Arguments.of(
                        6,
                        new Update()
                                .setEditedMessage(new Message()
                                        .setChat(new Chat()
                                                .setId(6)
                                        )
                                )
                ),
                Arguments.of(
                        5,
                        new Update()
                                .setMessage(new Message()
                                        .setChat(new Chat()
                                                .setId(5)
                                        )
                                )
                                .setEditedMessage(new Message()
                                        .setChat(new Chat()
                                                .setId(6)
                                        )
                                )
                )
        );
    }
}