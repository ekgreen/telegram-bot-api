package com.goodboy.telegram.bot.spring.impl.context;

import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.context.TelegramApiProviderContext;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

@Getter
@Infrastructure
public class SpringTelegramApiProviderContext implements TelegramApiProviderContext {

    private final @Nullable BeanDefinitionRegistry registry;

    private final @Nullable ConfigurableListableBeanFactory beanFactory;

    private final Environment environment;

    public SpringTelegramApiProviderContext(@Nullable BeanDefinitionRegistry registry, @Nullable ConfigurableListableBeanFactory beanFactory, Environment environment) {
        this.registry = registry;
        this.beanFactory = beanFactory;
        this.environment = environment;
    }
}
