/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.spring.impl.context;

import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.context.TelegramApiProviderContext;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
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
