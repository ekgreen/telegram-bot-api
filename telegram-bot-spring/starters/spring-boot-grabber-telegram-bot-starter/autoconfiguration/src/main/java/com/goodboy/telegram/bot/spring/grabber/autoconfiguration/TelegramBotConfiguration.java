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
import com.goodboy.telegram.bot.http.api.client.configuration.TelegramApiConfiguration;
import com.goodboy.telegram.bot.http.api.client.configuration.TelegramHttpClientProperties;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiContextHandler;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiContextResolver;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiThreadLocalContextHandlerImpl;
import com.goodboy.telegram.bot.http.api.client.extended.ExtendedTelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.extended.PathScanningExtendedTelegramClient;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpCommandBasedHandler;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpFileBasedHandler;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpRequestTypeBasedHandler;
import com.goodboy.telegram.bot.http.api.client.token.ContextBasedTokenResolver;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import com.goodboy.telegram.bot.http.api.client.update.ModifiableThreadLocalUpdateProvider;
import com.goodboy.telegram.bot.http.api.client.update.ModifiableUpdateProvider;
import com.goodboy.telegram.bot.spring.api.processor.HeavyweightScheduler;
import com.goodboy.telegram.bot.spring.impl.processors.annotations.webhook.NotificationHeavyweightScheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Configuration
@ComponentScan("com.goodboy.telegram.bot.spring")
public class TelegramBotConfiguration {

    @Bean @Primary
    public TelegramHttpClient telegramHttpClient(
            @Nonnull @Qualifier("httpClientAdapter") HttpClientAdapter adapter,
            @Nonnull TelegramApiConfiguration configuration,
            @Nonnull List<HttpClientAdapterCallback> mappers,
            @Nonnull List<HttpRequestTypeBasedHandler> handlers,
            @Nonnull TelegramApiTokenResolver tokenResolver
    ) {
        return new BaseTelegramHttpClient(
                adapter,
                configuration,
                mappers,
                handlers,
                tokenResolver
        );
    }

    @Bean
    public TelegramHttpClient longPollingTelegramHttpClient(
            @Nonnull @Qualifier("httpLongPollingClientAdapter") HttpClientAdapter adapter,
            @Nonnull TelegramApiConfiguration configuration,
            @Nonnull List<HttpClientAdapterCallback> mappers,
            @Nonnull List<HttpRequestTypeBasedHandler> handlers,
            @Nonnull TelegramApiTokenResolver tokenResolver
    ) {
        return new BaseTelegramHttpClient(
                adapter,
                configuration,
                mappers,
                handlers,
                tokenResolver
        );
    }

    @Bean
    public ExtendedTelegramHttpClient extendedTelegramHttpClient(TelegramHttpClient client) {
        return new PathScanningExtendedTelegramClient(client);
    }

    @Bean
    public TelegramApiConfiguration propertyTelegramApiConfiguration() {
        return new PropertyTelegramApiConfiguration(new TelegramHttpClientProperties());
    }

    @Bean
    public HttpClientAdapterCallback httpGetCallback(ObjectMapper objectMapper) {
        return new GetCallback(new TelegramApiToAdapterGetRequestMapper(objectMapper));
    }

    @Bean
    public HttpClientAdapterCallback httpPostCallback(ObjectMapper objectMapper) {
        return new PostCallback(new TelegramApiToAdapterPostRequestMapper(objectMapper));
    }

    @Bean
    public HttpClientAdapterCallback httpMultipartCallback(ObjectMapper objectMapper) {
        return new MultipartCallback(new TelegramApiToAdapterMultipartRequestMapper(objectMapper));
    }

    @Bean
    public HttpRequestTypeBasedHandler httpCommandBasedHandler(ObjectMapper objectMapper) {
        return new HttpCommandBasedHandler(objectMapper);
    }

    @Bean
    public HttpRequestTypeBasedHandler httpFileBasedHandler() {
        return new HttpFileBasedHandler();
    }

    @Bean @ConditionalOnMissingBean
    public TelegramApiTokenResolver contextTelegramApiTokenResolver(TelegramApiContextResolver telegramApiContextResolver) {
        return new ContextBasedTokenResolver(telegramApiContextResolver);
    }

    @Bean @ConditionalOnMissingBean
    public TelegramApiContextHandler threadLocalTelegramApiContextResolver() {
        return new TelegramApiThreadLocalContextHandlerImpl();
    }

    @Bean @ConditionalOnMissingBean
    public ModifiableUpdateProvider modifiableThreadLocalUpdateProvider() {
        return new ModifiableThreadLocalUpdateProvider();
    }

    @Bean
    public HeavyweightScheduler heavyweightScheduler(TelegramChatActionApi api) {
        return new NotificationHeavyweightScheduler(new ConcurrentLinkedQueue<>(), api);
    }

    @Bean @ConditionalOnMissingBean
    public ObjectMapper telegramBotServiceObjectMapper() {
        return new ObjectMapper()
                .enable(
                        DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                        DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
                        DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                        DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,
                        DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS
                )
                .enable(
                        JsonParser.Feature.ALLOW_SINGLE_QUOTES
                )
                .disable(
                        DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
                        DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS
                )
                .disable(
                        SerializationFeature.FAIL_ON_EMPTY_BEANS,
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
                )
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .registerModule(new JavaTimeModule())
                ;
    }

}
