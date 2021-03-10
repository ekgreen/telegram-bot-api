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

package com.goodboy.telegram.bot.spring.impl.events;

import com.goodboy.telegram.bot.spring.api.events.BotEventFactory;
import com.goodboy.telegram.bot.spring.api.events.BotRegisteredEvent;
import com.goodboy.telegram.bot.spring.api.events.BotsReadyEvent;
import com.goodboy.telegram.bot.spring.api.events.OnBotRegistry;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.processor.BotRegistryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Infrastructure
public class DirectingBotEventFactory implements BotEventFactory, ApplicationContextAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private ApplicationContext context;
    private List<OnBotRegistry> onBotRegistryListeners;

    @Override
    public void createOnRegistryEvent(@NotNull BotData botData) {
        if (onBotRegistryListeners != null && !onBotRegistryListeners.isEmpty()) {
            final BotRegisteredEvent botRegisteredEvent = new BotRegisteredEvent(this, botData);

            onBotRegistryListeners.forEach(listener -> listener.onRegistry(botRegisteredEvent));
        }
    }

    @Override
    public void createBotsReadyEvent(BotRegistryService botRegistryService) {
        applicationEventPublisher.publishEvent(new BotsReadyEvent(this, botRegistryService, context));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Autowired
    public void setOnBotRegistryListeners(List<OnBotRegistry> onBotRegistryListeners) {
        this.onBotRegistryListeners = onBotRegistryListeners;
    }

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
