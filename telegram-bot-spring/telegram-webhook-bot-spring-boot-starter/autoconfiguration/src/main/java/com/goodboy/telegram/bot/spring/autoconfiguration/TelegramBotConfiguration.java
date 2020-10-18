package com.goodboy.telegram.bot.spring.autoconfiguration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.goodboy.telegram.bot.api.client.HttpClientAdapter;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.method.webhook.UnauthorizedTelegramWebhookApi;
import com.goodboy.telegram.bot.core.client.HttpClientBuilder;
import com.goodboy.telegram.bot.core.method.webhook.UnauthorizedTelegramWebhookApiAdapter;
import com.goodboy.telegram.bot.spring.environment.ApplicationPropertyBotEnvironment;
import com.goodboy.telegram.bot.spring.environment.TelegramEnvironment;
import com.goodboy.telegram.bot.spring.meta.BotWebhookListener;
import com.goodboy.telegram.bot.spring.processors.WebhookBotRegistry;
import com.goodboy.telegram.bot.spring.providers.EnvironmentUrlResolver;
import com.goodboy.telegram.bot.spring.providers.UrlResolver;
import com.goodboy.telegram.bot.spring.servlets.TelegramBotServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Nonnull;
import javax.servlet.Servlet;
import java.net.http.HttpClient;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan("com.goodboy.telegram.bot.spring")
public class TelegramBotConfiguration {

    @Bean
    public ServletRegistrationBean<Servlet> telegramBotServletRegistration(TelegramBotServlet servlet, @Nonnull TelegramEnvironment environment) {
        return new ServletRegistrationBean<>(servlet, environment.getRootContext() + "/*");
    }

    @Bean
    public TelegramBotServlet telegramBotServlet(@Nonnull ObjectMapper mapper) {
        return new TelegramBotServlet(mapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public TelegramEnvironment telegramBotEnvironment(@Nonnull Environment environment) {
        return new ApplicationPropertyBotEnvironment(environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public UrlResolver telegramBotUrlResolver(@Nonnull TelegramEnvironment environment) {
        return new EnvironmentUrlResolver(environment);
    }

    @Bean
    public BotWebhookListener telegramBotWebhookRegistry(@Nonnull UrlResolver urlResolver, @Nonnull UnauthorizedTelegramWebhookApi webhookApi) {
        return new WebhookBotRegistry(urlResolver, webhookApi);
    }

    @Bean
    @ConditionalOnMissingBean
    public UnauthorizedTelegramWebhookApi telegramBotUnauthorisedWebhookApi(@Nonnull TelegramHttpClient client){
        return new UnauthorizedTelegramWebhookApiAdapter(client);
    }

    @Bean
    @ConditionalOnMissingBean
    public TelegramHttpClient telegramBotHttpClient(@Nonnull ObjectMapper mapper, @Nonnull HttpClientAdapter adapter){
        return HttpClientBuilder.newBuilder()
                .remote("https://api.telegram.org")
                .executor(adapter)
                .mapper(mapper)
                .build()
                ;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpClientAdapter telegramBotHttpClientAdapter(@Nonnull ObjectMapper mapper, @Value("${telegram.api.client.thread_pool_size:4}") Integer threadPoolSize){
        final HttpClient client = HttpClient.newBuilder()
                .executor(Executors.newFixedThreadPool(threadPoolSize))
                .version(HttpClient.Version.HTTP_2)
                .build();

        return new JavaHttpClientAdapter(client, mapper);
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
                        SerializationFeature.FAIL_ON_EMPTY_BEANS
                )
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                ;
    }

}
