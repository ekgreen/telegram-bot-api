package com.goodboy.telegram.bot.spring.processors;

import com.goodboy.telegram.bot.spring.meta.BotWebhookListener;
import com.goodboy.telegram.bot.spring.meta.Webhook;
import com.goodboy.telegram.bot.spring.meta.WebhookApi;
import com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition;
import com.goodboy.telegram.bot.spring.providers.*;
import com.goodboy.telegram.bot.spring.toolbox.InfrastructureBean;
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
import org.springframework.core.env.Environment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@InfrastructureBean
public class WebhookAnnotationResolver implements BeanPostProcessor, ApplicationContextAware {

    private final Map<String, WebhookBeanDefinition> definitions = new HashMap<>();

    private final List<BotWebhookListener> listeners;
    private final Environment environment;
    private final Supplier<TokenHandler> defaultStaticTokenHandler;
    private final Supplier<CertificateProvider> defaultCertificateProvider;

    private ApplicationContext context;

    @Autowired
    public WebhookAnnotationResolver(@Nullable List<BotWebhookListener> listeners, Environment environment) {
        this.listeners = listeners;
        this.environment = environment;
        this.defaultStaticTokenHandler = Suppliers.memoize(() -> {
            try {
                Constructor<ApplicationPropertyTokenHandler> constructor = ApplicationPropertyTokenHandler.class.getDeclaredConstructor(Environment.class);
                constructor.setAccessible(true);
                return constructor.newInstance(environment);
            } catch (Exception exception) {
                throw new BeanCreationException("could not create default token handler { handler = ApplicationPropertyTokenHandler.class }", exception);
            }
        });
        this.defaultCertificateProvider = Suppliers.memoize(() -> {
            try {
                Constructor<ApplicationPropertyCertificateProvider> constructor = ApplicationPropertyCertificateProvider.class.getDeclaredConstructor(Environment.class);
                constructor.setAccessible(true);
                return constructor.newInstance(environment);
            } catch (Exception exception) {
                throw new BeanCreationException("could not create default token handler { handler = ApplicationPropertyCertificateProvider.class }", exception);
            }
        });
    }

    public Object postProcessBeforeInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        @Nullable WebhookApi api;
        @Nonnull Class<?> type = bean.getClass();

        if (Webhook.class.isAssignableFrom(type) && (api = type.getAnnotation(WebhookApi.class)) != null) {
            definitions.put(beanName, createBeanDefinition(beanName, api));
        }

        return bean;
    }

    public Object postProcessAfterInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {

        // mb call after application refresh(?)
        definitions.computeIfPresent(beanName, (name, definition) -> {
            // call listeners
            if (listeners != null)
                listeners.forEach(listener -> listener.onWebhookCreation((Webhook) bean, definition));  // if Proxy212 ?

            return definition;
        });

        return bean;
    }


    @SneakyThrows
    private WebhookBeanDefinition createBeanDefinition(@Nonnull String beanName, @Nonnull WebhookApi api) {
        // bot name
        final String name = StringUtils.isNotEmpty(api.bot()) ? api.bot() : beanName;

        // webhook file definition
        final WebhookBeanDefinition propertyConfigurableDefinition = TelegramBotConfigurableDefinitions.BotDefinition.createWebhookBotDefinition(name, environment);

        // path pattern for http servlet
        final String path;

        if (StringUtils.isNotEmpty(api.path()))
            path = api.path();
        else if (StringUtils.isNotEmpty(api.value()))
            path = api.value();
        else if (StringUtils.isNotEmpty(propertyConfigurableDefinition.getPath()))
            path = propertyConfigurableDefinition.getPath();
        else
            path = name;

        // token resolver
        TokenHandler tokenHandler;

        if (StringUtils.isNotEmpty(api.token()))
            tokenHandler = (botName) -> api.token();
        else if (StringUtils.isNotEmpty(api.tokenHandlerBean())) {
            @Nonnull String tokenHandlerBeanName = api.tokenHandlerBean();
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
        WebhookBeanDefinition.RegistryBot registryBot = api.selfRegistryBot();

        if(registryBot == WebhookBeanDefinition.RegistryBot.NON){
            registryBot = propertyConfigurableDefinition.getSelfRegistry();
        }

        // certificate
        CertificateProvider certificateProvider;

        @Nonnull Class<? extends CertificateProvider> certificateTypeProvider = api.certificate();

        if(ApplicationPropertyCertificateProvider.class.isAssignableFrom(certificateTypeProvider)){
            certificateProvider = defaultCertificateProvider.get();
        } else {
            certificateProvider = certificateTypeProvider.getConstructor().newInstance();
        }

        // max connections
        final Integer maxConnections = api.maxConnections() >= 0 ? (Integer) api.maxConnections() : propertyConfigurableDefinition.getMaxConnections();

        return new WebhookBeanDefinition()
                .setBotName(name)
                .setPath(path)
                .setTokenHandler(tokenHandler)
                .setSelfRegistry(registryBot)
                .setCertificateProvider(certificateProvider)
                .setMaxConnections(maxConnections);
    }

    @RequiredArgsConstructor
    private static class BotNameAwareTokenHandler implements TokenHandler{

        private final String bot;
        private final TokenHandler origin;

        @Override
        public String token(@Nonnull String botName) {
            if(!bot.equals(botName))
                throw new IllegalStateException(String.format("incorrect bot name { expected = %s, incoming = %s }", botName, botName));

            return origin.token(botName);
        }
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
