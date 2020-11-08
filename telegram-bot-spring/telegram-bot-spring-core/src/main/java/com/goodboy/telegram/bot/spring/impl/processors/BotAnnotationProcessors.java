package com.goodboy.telegram.bot.spring.impl.processors;

import com.goodboy.telegram.bot.spring.api.environment.TelegramEnvironment;
import com.goodboy.telegram.bot.spring.api.handlers.token.TokenHandler;
import com.goodboy.telegram.bot.spring.api.listeners.OnBotCreationListener;
import com.goodboy.telegram.bot.spring.api.bot.Bot;
import com.goodboy.telegram.bot.spring.api.meta.BotCommand;
import com.goodboy.telegram.bot.spring.api.meta.WebhookBeanDefinition;
import com.goodboy.telegram.bot.spring.api.providers.CertificateProvider;
import com.goodboy.telegram.bot.spring.impl.providers.ApplicationPropertyCertificateProvider;
import com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition;
import com.goodboy.telegram.bot.spring.api.meta.InfrastructureBean;
import com.goodboy.telegram.bot.spring.impl.providers.ApplicationPropertyTokenHandler;
import com.google.common.base.Suppliers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@InfrastructureBean
public class BotAnnotationProcessors implements BeanPostProcessor, ApplicationContextAware {

    private final Map<String, BotBeanDefinition> definitions = new HashMap<>();

    private final List<OnBotCreationListener> listeners;
    private final TelegramEnvironment environment;
    private final Supplier<TokenHandler> defaultStaticTokenHandler;
    private final Supplier<CertificateProvider> defaultCertificateProvider;

    private ApplicationContext context;

    @Autowired
    public BotAnnotationProcessors(@Nullable List<OnBotCreationListener> listeners, TelegramEnvironment environment) {
        this.listeners = listeners;
        this.environment = environment;
        this.defaultStaticTokenHandler = Suppliers.memoize(() -> {
            try {
                Constructor<ApplicationPropertyTokenHandler> constructor = ApplicationPropertyTokenHandler.class.getDeclaredConstructor(TelegramEnvironment.class);
                constructor.setAccessible(true);
                return constructor.newInstance(environment);
            } catch (Exception exception) {
                throw new BeanCreationException("could not create default token handler { handler = ApplicationPropertyTokenHandler.class }", exception);
            }
        });
        this.defaultCertificateProvider = Suppliers.memoize(() -> {
            try {
                Constructor<ApplicationPropertyCertificateProvider> constructor = ApplicationPropertyCertificateProvider.class.getDeclaredConstructor(TelegramEnvironment.class);
                constructor.setAccessible(true);
                return constructor.newInstance(environment);
            } catch (Exception exception) {
                throw new BeanCreationException("could not create default token handler { handler = ApplicationPropertyCertificateProvider.class }", exception);
            }
        });
    }

    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        @Nullable com.goodboy.telegram.bot.spring.api.meta.Bot api;
        @NotNull Class<?> type = bean.getClass();

        if (Bot.class.isAssignableFrom(type) && (api = type.getAnnotation(com.goodboy.telegram.bot.spring.api.meta.Bot.class)) != null) {
            definitions.put(beanName, createBeanDefinition(beanName, api));
        }

        return bean;
    }

    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {

        // mb call after application refresh(?)
        definitions.computeIfPresent(beanName, (name, definition) -> {
            // call listeners
            if (listeners != null)
                listeners.forEach(listener -> listener.onBotCreation((Bot) bean, definition));  // if Proxy212 ?

            return definition;
        });

        return bean;
    }

    @SneakyThrows
    private BotBeanDefinition createBeanDefinition(@NotNull String beanName, @NotNull com.goodboy.telegram.bot.spring.api.meta.Bot api) {
        // bot name
        final String name = StringUtils.isNotEmpty(api.name()) ? api.name() : StringUtils.isNotEmpty(api.value()) ? api.value() : beanName;

        final var webhook = api.hook();
        // webhook file definition
        final var environmentWebhook = environment.getBotByName(name).getWebhook();

        // path pattern for http servlet
        final String path;

        if (StringUtils.isNotEmpty(webhook.path()))
            path = webhook.path();
        else if (StringUtils.isNotEmpty(environmentWebhook.getPath()))
            path = environmentWebhook.getPath();
        else
            path = name;

        // token resolver
        TokenHandler tokenHandler;

        if (StringUtils.isNotEmpty(api.token()))
            tokenHandler = (botName) -> api.token();
        else if (StringUtils.isNotEmpty(api.tokenHandlerBean())) {
            @NotNull String tokenHandlerBeanName = api.tokenHandlerBean();
            if (!context.containsBeanDefinition(tokenHandlerBeanName))
                throw new BeanCreationException("unresolved bean name on application context { bean_name = " + tokenHandlerBeanName + " }");

            final Supplier<TokenHandler> resolver = Suppliers.memoize(() -> context.getBean(tokenHandlerBeanName, TokenHandler.class));
            tokenHandler = (botName) -> resolver.get().token(botName);
        }  else {
            final Class<? extends TokenHandler> type = api.tokenHandler();

            if (ApplicationPropertyTokenHandler.class.equals(type)) {
                tokenHandler = defaultStaticTokenHandler.get();
            }else {
                tokenHandler = type.getConstructor().newInstance();
            }
        }
        tokenHandler = new BotNameAwareTokenHandler(name, tokenHandler);

        // self registry
        WebhookBeanDefinition.RegistryBot registryBot = webhook.selfRegistryHook();

        if(registryBot == WebhookBeanDefinition.RegistryBot.NON){
            registryBot = environmentWebhook.getRegistry();
        }

        // certificate
        CertificateProvider certificateProvider;

        @NotNull Class<? extends CertificateProvider> certificateTypeProvider = webhook.certificate();

        if(ApplicationPropertyCertificateProvider.class.isAssignableFrom(certificateTypeProvider)){
            certificateProvider = defaultCertificateProvider.get();
        } else {
            certificateProvider = certificateTypeProvider.getConstructor().newInstance();
        }

        // commands
        BotCommand[] commands = api.commands();

        // max connections
        final Integer maxConnections = webhook.maxConnections() >= 0 ? (Integer) webhook.maxConnections() : environmentWebhook.getMaxConnections();

        return new BotBeanDefinition()
                .setBotName(name)
                .setWebhookBeanDefinition(new WebhookBeanDefinition()
                        .setPath(path)
                        .setSelfRegistry(registryBot)
                        .setCertificateProvider(certificateProvider)
                        .setMaxConnections(maxConnections)
                )
                .setTokenHandler(tokenHandler)
                .setCommandsProvider(null);
    }

    @RequiredArgsConstructor
    private static class BotNameAwareTokenHandler implements TokenHandler{

        private final String bot;
        private final TokenHandler origin;

        @Override
        public String token(@NotNull String botName) {
            if(!bot.equals(botName))
                throw new IllegalStateException(String.format("incorrect bot name { expected = %s, incoming = %s }", botName, botName));

            return origin.token(botName);
        }
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
