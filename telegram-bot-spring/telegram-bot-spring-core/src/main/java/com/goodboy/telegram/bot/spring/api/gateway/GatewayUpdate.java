package com.goodboy.telegram.bot.spring.api.gateway;


import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.Update;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Wrapper around default Update by API Telegram with persisting state
 */
@RequiredArgsConstructor
public final class GatewayUpdate {

    private final @Nonnull Update update;

    // message text
    private String messageText;

    // message command
    private String command;

    /**
     * Return update object - update is immutable object, this is reason why we allow to produce it outside
     * but better to re-work it with overwriting necessary methods
     */
    public @Nonnull
    Update getUpdate() {
        return update;
    }

    /**
     * Method changes inner state of object - persisting calculated data
     * The reason - do not calculate heavy operations (with strings for instance) many times
     *
     * @return command or nil
     */
    public @Nullable
    String findMessageCommand() {
        // get from state (no null re-calc defend)
        if (this.command != null)
            return command;

        // else calculate
        final @Nullable String messageTextFromUpdate = findMessageTextFromUpdate();

        return this.command = cutCommandFromTextOrNull(messageTextFromUpdate);
    }

    /**
     * Method changes inner state of object - persisting calculated data
     * The reason - do not calculate heavy operations (with strings for instance) many times
     *
     * @return command or nil
     */
    public @Nullable
    String findMessageTextFromUpdate() {
        // get from state (no null re-calc defend)
        if (this.messageText != null)
            return messageText;

        return this.messageText = Stream.of(update.getMessage(), update.getEditedMessage(), update.getChannelPost(), update.getEditedChannelPost())
                .filter(Objects::nonNull)
                .findFirst()
                .map(Message::getText)
                .orElse(null);
    }

    private @Nullable
    String cutCommandFromTextOrNull(@Nullable String text) {
        if (StringUtils.isEmpty(text))
            return null;

        // mnemonic rules
        if (text.charAt(0) != '/')
            return null;


        // is this command
        final int text_length = text.length();

        for (int p = 1; p < text_length; p++) {
            final char c = text.charAt(p);

            if (c == ' ') {
                return p != text_length - 1 ? text.substring(1, p) : null;
            }
        }

        return null;
    }

}
