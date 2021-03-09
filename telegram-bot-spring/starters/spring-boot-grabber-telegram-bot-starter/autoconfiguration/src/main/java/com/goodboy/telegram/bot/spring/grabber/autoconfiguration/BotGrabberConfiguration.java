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

package com.goodboy.telegram.bot.spring.grabber.autoconfiguration;

import com.goodboy.telegram.bot.api.methods.updates.TelegramGetUpdatesApi;
import com.goodboy.telegram.bot.spring.api.gateway.Gateway;
import com.goodboy.telegram.bot.spring.grabber.ConcurrentGrabbersRunner;
import com.goodboy.telegram.bot.spring.grabber.GrabberDorm;
import com.goodboy.telegram.bot.spring.grabber.GrabberHome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Configuration
public class BotGrabberConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "telegram.grabber", name = "type", havingValue = "DORM")
    public ConcurrentGrabbersRunner grabberDorm(Environment environment, Gateway gateway, TelegramGetUpdatesApi api) {
        return new GrabberDorm(environment, gateway, api);
    }

    @Bean
    @ConditionalOnProperty(prefix = "telegram.grabber", name = "type", havingValue = "HOME")
    public ConcurrentGrabbersRunner grabberHome(Environment environment, Gateway gateway, TelegramGetUpdatesApi api) {
        return new GrabberHome(environment, gateway, api);
    }
}