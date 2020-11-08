package com.goodboy.telegram.bot.spring.autoconfiguration;

import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.method.command.TelegramCommandApi;
import com.goodboy.telegram.bot.api.method.message.TelegramMessageApi;
import com.goodboy.telegram.bot.api.method.token.TokenSupplier;
import com.goodboy.telegram.bot.api.method.webhook.TelegramWebhookApi;
import com.goodboy.telegram.bot.core.method.command.TelegramCommandApiAdapter;
import com.goodboy.telegram.bot.core.method.message.TelegramMessageApiAdapter;
import com.goodboy.telegram.bot.core.method.webhook.TelegramWebhookApiAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean
public class TelegramApiConfiguration {

    @Bean
    public TelegramWebhookApi telegramBotWebhookApi(@NotNull TelegramHttpClient client, @NotNull TokenSupplier supplier){
        return new TelegramWebhookApiAdapter(client, supplier);
    }

    @Bean
    public TelegramMessageApi telegramBotMessageApi(@NotNull TelegramHttpClient client, @NotNull TokenSupplier supplier){
        return new TelegramMessageApiAdapter(client, supplier);
    }

    @Bean
    public TelegramCommandApi telegramCommandApi(@NotNull TelegramHttpClient client, @NotNull TokenSupplier supplier) {
        return new TelegramCommandApiAdapter(client, supplier);
    }

    @Bean
    public TokenSupplier telegramBotTokenSupplier(@NotNull com.goodboy.telegram.bot.spring.api.handlers.token.TokenSupplier supplier){
        return supplier::getRequestToken;
    }
}
