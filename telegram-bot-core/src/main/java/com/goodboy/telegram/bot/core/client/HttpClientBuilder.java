package com.goodboy.telegram.bot.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.client.PathHandler;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.api.client.TelegramHttpClientInterceptor;
import com.goodboy.telegram.bot.api.method.token.TokenSupplier;
import com.goodboy.telegram.bot.core.method.message.FilePathHandler;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClientBuilder implements TelegramHttpClient.Builder {

    private final static ObjectMapper DEFAULT_JSON_ENCODER = new ObjectMapper();

    private final static Supplier<List<PathHandler<?>>> DEFAULT_PATH_HANDLERS
            = Suppliers.memoize(() -> Lists.newArrayList(new FilePathHandler()));

    private HttpClientAdapter adapter;

    private String host;

    private List<TelegramHttpClientInterceptor> interceptors;

    private List<PathHandler<?>> pathHandlers;

    private TokenSupplier tokenSupplier;

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
    public TelegramHttpClient.Builder mapper(ObjectMapper decoder) {
        this.mapper = decoder;
        return this;
    }

    @Override
    public TelegramHttpClient.Builder mapper(PathHandler<?> decoder) {
        if(pathHandlers == null)
            this.pathHandlers = new ArrayList<>();

        pathHandlers.add(decoder);

        return this;
    }

    @Override
    public TelegramHttpClient.Builder token(TokenSupplier tokenSupplier) {
        this.tokenSupplier = tokenSupplier;
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
                getJsonEncoder(),
                null,
                getPathHandlerWithDefault(pathHandlers),
                interceptors,
                tokenSupplier,
                host
        );
    }

    private HttpClientAdapter defaultTelegramHttpClientAdapter() {
        final OkHttpClient client = new OkHttpClient();

        return new OkHttpClientAdapter(
                client, getJsonEncoder(), new MultipartDataHandlerImpl(getJsonEncoder())
        );
    }

    private ObjectMapper getJsonEncoder() {
        return mapper == null ? DEFAULT_JSON_ENCODER : mapper;
    }

    private @NotNull
    List<PathHandler<?>> getPathHandlerWithDefault(@Nullable List<PathHandler<?>> pathHandlers) {
        final List<PathHandler<?>> handlers = new ArrayList<>(DEFAULT_PATH_HANDLERS.get());

        if(pathHandlers != null)
            handlers.addAll(pathHandlers);

        return handlers;
    }


}
