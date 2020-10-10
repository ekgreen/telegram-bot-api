package com.goodboy.telegram.bot.spring;

import com.google.common.base.Suppliers;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@InfrastructureBean
public class WebhookAnnotationResolver implements BeanPostProcessor, ApplicationContextAware {

    private final Map<String, WebhookBeanDefinition> definitions = new HashMap<>();

    private final List<BotWebhookListener> listeners;
    private final Supplier<TokenHandler> defaultStaticTokenHandler;

    private ApplicationContext context;

    @Autowired
    public WebhookAnnotationResolver(@Nullable List<BotWebhookListener> listeners, Environment environment) {
        this.listeners = listeners;
        this.defaultStaticTokenHandler = Suppliers.memoize(() -> {
            try {
                return ApplicationPropertyTokenHandler.class.getDeclaredConstructor(Environment.class).newInstance(environment);
            } catch (Exception exception) {
                throw new BeanCreationException("could not create default token handler { handler = ApplicationPropertyTokenHandler.class }", exception);
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
            // set instance in definition
            definition.setWebhook((Webhook) bean); // if Proxy212 ?
            // call listeners
            if (listeners != null)
                listeners.forEach(listener -> listener.onWebhookCreation(definition));

            return definition;
        });

        return bean;
    }


    @SneakyThrows
    private WebhookBeanDefinition createBeanDefinition(@Nonnull String beanName, @Nonnull WebhookApi api) {
        // path pattern for http servlet
        final String path;

        if (StringUtils.isNotEmpty(api.path()))
            path = api.path();
        else if (StringUtils.isNotEmpty(api.value()))
            path = api.value();
        else
            throw new BeanCreationException("WebhookApi[" + beanName + "] api not contains path/value");

        // bot name
        final String name = StringUtils.isNotEmpty(api.bot()) ? api.bot() : beanName;

        // token resolver
        final TokenHandler tokenHandler;

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

        // certificate
        final String certificate = StringUtils.isNotEmpty(api.certificate()) ? api.certificate() : null;

        // max connections
        final Integer maxConnections = api.maxConnections() >= 0 ? api.maxConnections() : null;

        return new WebhookBeanDefinition()
                .setBotName(name)
                .setPath(path)
                .setTokenHandler(tokenHandler)
                .setSelfRegistry(api.selfRegistryBot())
                .setCertificate(certificate)
                .setMaxConnections(maxConnections);
    }



    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
