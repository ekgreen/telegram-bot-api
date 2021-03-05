package com.goodboy.telegram.bot.spring;

import com.goodboy.telegram.bot.api.Chat;
import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.Update;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class UpdateSource {

    static Stream<Arguments> chatUpdateSource() {
        return Stream.of(
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
