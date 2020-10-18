package com.goodboy.telegram.bot.spring.environment;

import com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition;
import com.goodboy.telegram.bot.spring.providers.TelegramBotConfigurableDefinitions;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

import static com.goodboy.telegram.bot.spring.providers.TelegramBotConfigurableDefinitions.TELEGRAM_CONF_ROOT_CONTEXT;
import static com.goodboy.telegram.bot.spring.providers.TelegramBotConfigurableDefinitions.TELEGRAM_BOTS_ROOT_CONTEXT;

@RequiredArgsConstructor
public class ApplicationPropertyBotEnvironment implements TelegramEnvironment {

    private final Environment environment;

    public @NotNull String getRootContext() {
        @Nullable String property;
        return (property = environment.getProperty(TELEGRAM_CONF_ROOT_CONTEXT)) != null ? property : TELEGRAM_BOTS_ROOT_CONTEXT;
    }

    public @NotNull String getBalanceProxy() {
        return TelegramBotConfigurableDefinitions.CallbackDefinition.getBalanceProxy(environment);
    }

    public @NotNull WebhookBeanDefinition getWebhookDefinition(@NotNull String botName) {
        return TelegramBotConfigurableDefinitions.BotDefinition.createWebhookBotDefinition(botName, environment);
    }
}
