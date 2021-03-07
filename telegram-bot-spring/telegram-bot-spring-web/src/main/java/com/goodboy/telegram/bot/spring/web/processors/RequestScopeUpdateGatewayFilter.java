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

package com.goodboy.telegram.bot.spring.web.processors;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.http.api.client.response.UpdateProvider;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.events.OnBotRegistry;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayFilter;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayFilterChain;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Infrastructure
public class RequestScopeUpdateGatewayFilter implements GatewayFilter, UpdateProvider {

    // cache for request scopes updates
    private final static ThreadLocal<Update> CACHE = new ThreadLocal<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, Update update, GatewayFilterChain chain) {
        try {
            // add in cache
            CACHE.set(update);
            return chain.invoke(proxy, method, args, update);
        }finally {
            // remove from cache
            CACHE.remove();
        }
    }

    @Override
    public Update getUpdate() {
        return CACHE.get();
    }

}
