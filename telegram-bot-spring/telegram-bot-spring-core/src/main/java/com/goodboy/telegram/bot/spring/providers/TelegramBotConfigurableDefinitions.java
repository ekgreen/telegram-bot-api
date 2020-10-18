package com.goodboy.telegram.bot.spring.providers;

import com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition;
import com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition.RegistryBot;
import com.goodboy.telegram.bot.spring.providers.ApplicationPropertyCertificateProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

import javax.annotation.Nonnull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramBotConfigurableDefinitions {

    /**
     * core prefix for telegram bot configurations
     */
    public static final String TELEGRAM_CONF_PREFIX = "telegram.api";

    /**
     * base uri prefix for telegram bot if not defined other is "/telegram/bot/
     */
    public static final String TELEGRAM_CONF_ROOT_CONTEXT = TELEGRAM_CONF_PREFIX + ".root-context";

    /**
     * base uri prefix for telegram bot if not defined other is "/telegram/bot/
     */
    public static final String TELEGRAM_BOTS_ROOT_CONTEXT = "/telegram/bot";

    /**
     * core prefix for telegram callback configurations
     */
    public static final String TELEGRAM_CALLBACK = TELEGRAM_CONF_PREFIX + ".callback";
    
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class CallbackDefinition{

        public static String getBalanceProxy(@Nonnull Environment environment) {
            return environment.getProperty(TELEGRAM_CALLBACK + ".proxy");
        }
    }

    /**
     * core prefix for telegram bots.{{botName}} configurations
     */
    public static final String TELEGRAM_BOTS = TELEGRAM_CONF_PREFIX + ".bots";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class BotDefinition{

        private static String botDefinitionPath(@Nonnull String botName) {
            return TELEGRAM_BOTS + "." + botName;
        }

        public static String getPath(@Nonnull String botName, @Nonnull Environment environment) {
            return environment.getProperty(botDefinitionPath(botName) + ".path");
        }

        public static RegistryBot isSelfRegistryBot(@Nonnull String botName, @Nonnull Environment environment) {
            @Nullable String property = environment.getProperty(botDefinitionPath(botName) + ".registry");
            return StringUtils.isNotEmpty(property) ? RegistryBot.valueOf(property) : RegistryBot.NON;
        }

        public static String getToken(@Nonnull String botName, @Nonnull Environment environment) {
            return environment.getProperty(botDefinitionPath(botName) + ".token");
        }

        public static String getCertificatePath(@Nonnull String botName, @Nonnull Environment environment) {
            return environment.getProperty(botDefinitionPath(botName) + ".certificate");
        }

        public static Integer getMaxConnections(@Nonnull String botName, @Nonnull Environment environment) {
            @Nullable String property;
            return (property = environment.getProperty(botDefinitionPath(botName) + ".maxConnections")) != null ? Integer.valueOf(property) : null;
        }

        public static WebhookBeanDefinition createWebhookBotDefinition(@Nonnull String botName, @Nonnull Environment environment) {
            return new WebhookBeanDefinition()
                    .setBotName(botName)
                    .setPath(getPath(botName, environment))
                    .setSelfRegistry(isSelfRegistryBot(botName, environment))
                    .setTokenHandler((bot) -> getToken(botName, environment))
                    .setCertificateProvider(new ApplicationPropertyCertificateProvider(environment))
                    .setMaxConnections(getMaxConnections(botName, environment));
        }
    }
}
