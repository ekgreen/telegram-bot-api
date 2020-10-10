package com.goodboy.telegram.bot.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public interface TelegramHttpClient {

    /**
     * Sends the given request using this client, blocking if necessary to get
     * the response. The returned {@link TelegramCoreResponse}{@code <T>} contains the
     * response status, headers, and body ( as handled by given response body
     * handler ).
     *
     * @param <V> the request body type
     * @param <T> the response body type
     * @param request the request
     *
     * @return the response
     */
    <T, V> TelegramCoreResponse<T> send(@Nonnull Request<V> request);

    /**
     * Sends the given request asynchronously using this client with the given
     * response body handler.
     *
     * @param <V> the request body type
     * @param <T> the response body type
     * @param request the request
     */
    <T, V> CompletableFuture<TelegramCoreResponse<T>> sendAsync(Request<V> request);

    interface Builder{

        /**
         * Sets the executor to be used for proxying http requests.
         *
         * <p> If this method is not invoked prior to {@linkplain #build()
         * building}, a default executor is created for each newly built {@code
         * HttpClientAdapter}.
         *
         * @implNote The default executor uses an adapted java http client
         * {@link java.net.http.HttpClient}
         *
         * @param executor the HttpClientAdapter
         * @return this builder
         */
        public Builder executor(HttpClientAdapter executor);

        /**
         * Sets the json response decoder to be used for response demarshalling.
         *
         * <p> If this method is not invoked prior to {@linkplain #build()
         * building}, a default decoder is created once {@code
         * ObjectMapper}.
         *
         * @param decoder the ObjectMapper
         * @return this builder
         */
        public Builder decoder(ObjectMapper decoder);

        /**
         * Sets the path response decoder to be used for response demarshalling.
         *
         * <p> If this method is not invoked prior to {@linkplain #build()
         * building}, a default decoder is created once {@code
         * PathHandler}.
         *
         * @param decoder the PathHandler
         * @return this builder
         */
        public Builder decoder(PathHandler<?> decoder);

        /**
         * Sets the telegram remote endpoint
         *
         * <p> If this method is not invoked prior to {@linkplain #build()
         * building}, a default endpoint is <a href="https://api.telegram.org/">telegram</a>
         *
         * @param hostPort the host port of remote endpoint
         * @return this builder
         */
        public Builder remote(@Nonnull String hostPort);

        /**
         * The token is a string along the lines of 110201543:AAHdqTcvCH1vGWJxfSeofSAs0K5PALDsaw that is required
         * to authorize the bot and send requests to the Bot API. Keep your token secure and store it safely,
         * it can be used by anyone to control your bot.
         *
         * Linking btw client instance and specific bot
         */
        public Builder token(String token);

        /**
         * Sets the telegram request interceptor
         *
         * @param interceptor the interceptor
         * @return this builder
         */
        public Builder interceptor(@Nonnull TelegramHttpClientInterceptor interceptor);

        /**
         * Returns a new {@link TelegramHttpClient} built from the current state of this
         * builder.
         *
         * @return a new {@code HttpClientAdapter}
         */
        TelegramHttpClient build();
    }
}
