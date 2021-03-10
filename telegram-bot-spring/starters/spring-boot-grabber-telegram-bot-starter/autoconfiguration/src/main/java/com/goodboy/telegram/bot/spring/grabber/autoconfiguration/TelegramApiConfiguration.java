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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.goodboy.telegram.bot.api.methods.action.TelegramChatActionApi;
import com.goodboy.telegram.bot.api.methods.updates.TelegramGetUpdatesApi;
import com.goodboy.telegram.bot.http.api.client.BaseTelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapterCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.get.GetCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.get.TelegramApiToAdapterGetRequestMapper;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.TelegramApiToAdapterMultipartRequestMapper;
import com.goodboy.telegram.bot.http.api.client.adapter.post.PostCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.post.TelegramApiToAdapterPostRequestMapper;
import com.goodboy.telegram.bot.http.api.client.configuration.PropertyTelegramApiConfiguration;
import com.goodboy.telegram.bot.http.api.client.configuration.TelegramHttpClientProperties;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiContextHandler;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiContextResolver;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiThreadLocalContextHandlerImpl;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpCommandBasedHandler;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpFileBasedHandler;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpRequestTypeBasedHandler;
import com.goodboy.telegram.bot.http.api.client.token.ContextBasedTokenResolver;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import com.goodboy.telegram.bot.http.api.method.action.TelegramChatActionImpl;
import com.goodboy.telegram.bot.http.api.method.updates.TelegramGetUpdatesImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Configuration
public class TelegramApiConfiguration {

    @Bean
    public TelegramGetUpdatesApi telegramGetUpdatesApi(@Qualifier("longPollingTelegramHttpClient") @Nonnull TelegramHttpClient client) {
        return new TelegramGetUpdatesImpl(client);
    }

    @Bean
    public TelegramChatActionApi telegramChatActionApi(@Nonnull TelegramHttpClient client) {
        return new TelegramChatActionImpl(client);
    }

}
