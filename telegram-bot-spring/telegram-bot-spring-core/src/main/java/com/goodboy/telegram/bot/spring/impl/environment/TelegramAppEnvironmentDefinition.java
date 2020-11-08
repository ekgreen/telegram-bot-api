package com.goodboy.telegram.bot.spring.impl.environment;

import com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition;
import com.goodboy.telegram.bot.spring.api.meta.WebhookBeanDefinition;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "telegram.api")
public class TelegramAppEnvironmentDefinition {

    private String rootContext;

    private Callback callback = new Callback();

    private Map<String, Bot> bots = new HashMap<>();

    public @Nullable Bot getBot(@NotNull String botName) {
        return bots.get(botName);
    }

    @Data
    public static class Callback{
        String proxy;
    }

    @Data
    public static class Bot{

        String token;

        Webhook webhook;
    }

    @Data
    public static class Webhook {

        String path;

        WebhookBeanDefinition.RegistryBot registry;

        String certificate;

        Integer maxConnections;
    }

}
