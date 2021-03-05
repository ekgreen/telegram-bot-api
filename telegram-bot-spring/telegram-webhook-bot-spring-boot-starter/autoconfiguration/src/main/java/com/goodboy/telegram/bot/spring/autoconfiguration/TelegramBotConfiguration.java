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

package com.goodboy.telegram.bot.spring.autoconfiguration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.goodboy.telegram.bot.api.meta.Multipart;
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
import com.goodboy.telegram.bot.http.api.client.handlers.HttpCommandBasedHandler;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpFileBasedHandler;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpRequestTypeBasedHandler;
import com.goodboy.telegram.bot.http.api.client.token.ContextBasedTokenResolver;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Configuration
@EnableScheduling
@ComponentScan("com.goodboy.telegram.bot.spring")
public class TelegramBotConfiguration {

    @Bean @ConditionalOnMissingBean
    public TelegramHttpClient telegramHttpClient(
            @Nonnull HttpClientAdapter adapter,
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
                .registerModule(localDateTimeModule())
                ;
    }

    private Module localDateTimeModule() {
        JavaTimeModule module = new JavaTimeModule();

        LocalDateTimeDeserializer deserializer = new MillisOrLocalDateTimeDeserializer();
        module.addDeserializer(LocalDateTime.class, deserializer);

        LocalDateTimeSerializer serializer = new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        module.addSerializer(LocalDateTime.class, serializer);

        return module;
    }

    private static class MillisOrLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

        public MillisOrLocalDateTimeDeserializer() {
            super(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                long value = parser.getValueAsLong();
                Instant instant = Instant.ofEpochMilli(value);

                return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            }

            return super.deserialize(parser, context);
        }

    }

}
