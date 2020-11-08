package com.goodboy.telegram.bot.spring.impl.environment;

import com.goodboy.telegram.bot.spring.api.environment.TelegramEnvironment;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@RequiredArgsConstructor
public class ApplicationPropertyBotEnvironment implements TelegramEnvironment {

    /**
     * base uri prefix for telegram bot if not defined other is "/telegram/bot/
     */
    public static final String TELEGRAM_BOTS_ROOT_CONTEXT = "/telegram/bot";

    private final TelegramAppEnvironmentDefinition environment;

    public @NotNull String getRootContext() {
        @Nullable String property;
        return (property = environment.getRootContext()) != null ? property : TELEGRAM_BOTS_ROOT_CONTEXT;
    }

    public @NotNull String getBalanceProxy() {
        return Objects.requireNonNull(environment.getCallback().getProxy());
    }

    public @NotNull TelegramAppEnvironmentDefinition.Bot getBotByName(@NotNull String botName) {
        return Objects.requireNonNull(environment.getBot(botName));
    }
}
