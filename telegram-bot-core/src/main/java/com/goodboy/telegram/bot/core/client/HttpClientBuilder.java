package com.goodboy.telegram.bot.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.client.PathHandler;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.client.HttpClientAdapter;
import com.goodboy.telegram.bot.api.client.TelegramHttpClientInterceptor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClientBuilder implements TelegramHttpClient.Builder {

    private final static ObjectMapper DEFAULT_JSON_ENCODER = new ObjectMapper();

    private HttpClientAdapter adapter;

    private String host;

    private List<TelegramHttpClientInterceptor> interceptors;

    private List<PathHandler<?>> pathHandlers;

    private ObjectMapper mapper;

    public static TelegramHttpClient.Builder newBuilder(){
        return new HttpClientBuilder();
    }

    public static TelegramHttpClient defaultHttpClient(){
        return newBuilder().build();
    }

    @Override
    public TelegramHttpClient.Builder executor(HttpClientAdapter executor) {
        this.adapter = executor;
        return this;
    }

    @Override
    public TelegramHttpClient.Builder decoder(ObjectMapper decoder) {
        this.mapper = decoder;
        return this;
    }

    @Override
    public TelegramHttpClient.Builder decoder(PathHandler<?> decoder) {
        if(pathHandlers == null)
            this.pathHandlers = new ArrayList<>();

        pathHandlers.add(decoder);

        return this;
    }

    @Override
    public TelegramHttpClient.Builder remote(@NotNull String hostPort) {
        this.host = hostPort;
        return this;
    }

    @Override
    public TelegramHttpClient.Builder interceptor(@NotNull TelegramHttpClientInterceptor interceptor) {
        if(interceptors == null)
            this.interceptors = new ArrayList<>();

        interceptors.add(interceptor);

        return this;
    }

    @Override
    public TelegramHttpClient build() {
        return new TelegramHttpClientImpl(
                adapter == null ?  defaultTelegramHttpClientAdapter(): adapter,
                mapper == null ? DEFAULT_JSON_ENCODER : mapper,
                pathHandlers,
                interceptors,
                host
        );
    }

    private HttpClientAdapter defaultTelegramHttpClientAdapter() {
        return new JavaHttpClientAdapter(
                HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_2)
                        .build(),
                mapper == null ? DEFAULT_JSON_ENCODER : mapper
        );
    }
}
