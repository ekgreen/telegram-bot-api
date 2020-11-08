package com.goodboy.telegram.bot.spring.autoconfiguration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.goodboy.telegram.bot.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.client.adapter.MultipartHttpClientHandler;
import com.goodboy.telegram.bot.api.method.webhook.TelegramWebhookApi;
import com.goodboy.telegram.bot.core.client.HttpClientBuilder;
import com.goodboy.telegram.bot.core.client.MultipartDataHandlerImpl;
import com.goodboy.telegram.bot.core.client.OkHttpClientAdapter;
import com.goodboy.telegram.bot.spring.impl.environment.ApplicationPropertyBotEnvironment;
import com.goodboy.telegram.bot.spring.api.environment.TelegramEnvironment;
import com.goodboy.telegram.bot.spring.api.listeners.OnBotCreationListener;
import com.goodboy.telegram.bot.spring.impl.listeners.WebhookBotRegistry;
import com.goodboy.telegram.bot.spring.impl.providers.EnvironmentUrlResolver;
import com.goodboy.telegram.bot.spring.api.providers.UrlResolver;
import com.goodboy.telegram.bot.spring.impl.servlets.TelegramBotServlet;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import org.jetbrains.annotations.NotNull;

import javax.servlet.Servlet;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Configuration
@Import(TelegramApiConfiguration.class)
@ComponentScan("com.goodboy.telegram.bot.spring")
public class TelegramBotConfiguration {

    @Bean
    public ServletRegistrationBean<Servlet> telegramBotServletRegistration(TelegramBotServlet servlet, @NotNull TelegramEnvironment environment) {
        return new ServletRegistrationBean<>(servlet, environment.getRootContext() + "/*");
    }

    @Bean
    public TelegramBotServlet telegramBotServlet(@NotNull ObjectMapper mapper) {
        return new TelegramBotServlet(mapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public TelegramEnvironment telegramBotEnvironment(@NotNull Environment environment) {
        return new ApplicationPropertyBotEnvironment(environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public UrlResolver telegramBotUrlResolver(@NotNull TelegramEnvironment environment) {
        return new EnvironmentUrlResolver(environment);
    }

    @Bean
    public OnBotCreationListener telegramBotWebhookRegistry(@NotNull UrlResolver urlResolver, @NotNull TelegramWebhookApi webhookApi) {
        return new WebhookBotRegistry(urlResolver, webhookApi);
    }

    @Bean
    @ConditionalOnMissingBean
    public TelegramHttpClient telegramBotHttpClient(@NotNull ObjectMapper mapper, @NotNull HttpClientAdapter adapter){
        return HttpClientBuilder.newBuilder()
                .remote("https://api.telegram.org")
                .executor(adapter)
                .mapper(mapper)
                .build()
                ;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClientAdapter telegramBotHttpClientAdapter(@NotNull ObjectMapper mapper, MultipartHttpClientHandler multipartHttpClientHandler){
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .build();

        return new OkHttpClientAdapter(client, mapper, multipartHttpClientHandler);
    }

    private Interceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public MultipartHttpClientHandler multipartHttpClientHandler(@NotNull ObjectMapper mapper){
        return new MultipartDataHandlerImpl(mapper);
    }

    @Bean
    @ConditionalOnMissingBean
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
