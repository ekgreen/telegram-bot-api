package com.goodboy.telegram.bot.http.api.client.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapterCallback;
import com.goodboy.telegram.bot.http.api.client.configuration.TelegramApiConfiguration;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpRequestTypeBasedHandler;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Common builders for creating {@link TelegramHttpClient} there is api library without http client realisation
 * therefore in all constructions u have to provide adapter {@link HttpClientAdapter}
 * <p>
 * API did not provide default adapter implementation as is.
 * <p>
 * It is simple interface without nine circles of interface hell for share each setter from another
 */
public interface TelegramHttpClientBuilder {

    /**
     * Absolutely minimum that you need to start work with {@link TelegramHttpClient}
     *
     * @param adapter http client adapter
     * @return telegram http client
     */
    public static TelegramHttpClient defaultLesslessTelegramHttpClient(@Nonnull HttpClientAdapter adapter){
        return builder().build(adapter);
    }

    /**
     * Tied on specific bot client {@link TelegramHttpClient}
     *
     * @param adapter http client adapter
     * @return telegram http client
     */
    public static TelegramHttpClient defaultTiedTelegramHttpClient(@Nonnull String token, @Nonnull HttpClientAdapter adapter){
        return builder().token(token).build(adapter);
    }

    /**
     * Method provides default http builder
     *
     * @return telegram http builder
     */
    public static TelegramHttpClientBuilder builder(){
        return new FilledTelegramHttpClientBuilder();
    }

    /**
     * Static token for http client - this means that all request will send by single bot
     *
     * @param token single bot token
     * @return builder
     */
    public TelegramHttpClientBuilder token(@Nonnull String token);

    /**
     * Token provider introduces more dynamic in your code - more applicable for frameworks with request scopes. Produce token
     * on each request
     *
     * @param resolver token resolver & producer
     * @return builder
     */
    public TelegramHttpClientBuilder token(@Nonnull TelegramApiTokenResolver resolver);

    /**
     * Configuration needs you to specify extra logic for telegram http client, for instance set behaviour of client
     * on non 2** http response
     *
     * @param configuration telegram http client configuration
     * @return builder
     */
    public TelegramHttpClientBuilder configuration(@Nonnull TelegramApiConfiguration configuration);

    /**
     * Callback is more part of inner client api, but steal available for users. If u enjoy overwrite default callbacks
     * and create your own logic on any http method this method for u
     *
     * @param callback http method callback
     * @return builder
     */
    public TelegramHttpClientBuilder callback(@Nonnull HttpClientAdapterCallback callback);

    /**
     * Such as {@link this#callback(HttpClientAdapterCallback)} but list
     *
     * @param callbacks http method callbacks
     * @return builder
     */
    public TelegramHttpClientBuilder callbacks(@Nonnull List<HttpClientAdapterCallback> callbacks);

    /**
     * Like callback part of inner api, but available for modification. Specify extra logic on handling response
     * of different types telegram bot api
     *
     * @param handler telegram bot api response handler
     * @return build
     */
    public TelegramHttpClientBuilder requestHandler(@Nonnull HttpRequestTypeBasedHandler handler);

    /**
     * Such as {@link this#requestHandler(HttpRequestTypeBasedHandler)} but list
     *
     * @param handlers telegram bot api response handlers
     * @return builder
     */
    public TelegramHttpClientBuilder requestHandlers(@Nonnull List<HttpRequestTypeBasedHandler> handlers);

    /**
     * For constructing default parts of telegram http client uses json object mapper. U can provide your own realisation
     * or skip and use default
     *
     * @param mapper json object mapper
     * @return       builder
     */
    public TelegramHttpClientBuilder objectMapper(@Nonnull ObjectMapper mapper);

    /**
     * Build http client
     *
     * @param adapter http client adapter
     * @return client
     */
    public TelegramHttpClient build(@Nonnull HttpClientAdapter adapter);

    /**
     * Builder extension for library implementations
     */
    default public TelegramHttpClient build(){
        throw new UnsupportedOperationException("extend me");
    }
}
