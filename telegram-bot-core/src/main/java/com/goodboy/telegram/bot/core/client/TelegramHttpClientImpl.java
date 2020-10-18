package com.goodboy.telegram.bot.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.client.*;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toMap;

@Slf4j
@RequiredArgsConstructor
public class TelegramHttpClientImpl implements TelegramHttpClient {

    /**
     * Default endpoint is <a href="https://api.telegram.org/">telegram</a>
     * Will replaced by null host value
     */
    private final static String DEFAULT_HOST = "https://api.telegram.org";

    private final HttpClientAdapter adapter;
    private final ObjectMapper decoder;
    private final Map<String, PathHandler<?>> pathHandlers;
    private final List<TelegramHttpClientInterceptor> interceptors;
    private final String url;
    private final String token;

    public TelegramHttpClientImpl(
            HttpClientAdapter adapter,
            ObjectMapper decoder
    ){
        this(adapter, decoder, (List<PathHandler<?>>) null, null, DEFAULT_HOST, null);
    }

    public TelegramHttpClientImpl(
            HttpClientAdapter adapter,
            ObjectMapper decoder,
            @Nullable List<PathHandler<?>> pathHandlers,
            @Nullable List<TelegramHttpClientInterceptor> interceptors,
            @Nullable String url,
            @Nullable String token
    ) {
        this.adapter = adapter;
        this.decoder = decoder;
        this.pathHandlers = pathHandlers != null ? pathHandlers.stream().collect(toMap(
                PathHandler::handlingOn,
                handler -> handler
        )) : ImmutableMap.of();
        this.interceptors = interceptors != null ? interceptors : ImmutableList.of();
        this.url = StringUtils.isNotBlank(url) ? url : DEFAULT_HOST;
        this.token = token;
    }

    @RequiredArgsConstructor
    private static class AggregationTelegramHttpClient implements TelegramHttpClientInterceptorChain {

        private final TelegramHttpClientInterceptorChain origin;
        private final List<TelegramHttpClientInterceptor> interceptors;
        private int bound;

        @Override
        public <T, V> TelegramCoreResponse<T> intercept(Request<V> request) {
            if (bound == interceptors.size()) {
                return origin.intercept(request);
            } else {
                return interceptors.get(bound++).intercept(request, this);
            }
        }
    }

    @Override
    public <T, V> TelegramCoreResponse<T> send(@NotNull Request<V> request) {
        return new AggregationTelegramHttpClient(this::sync, interceptors).intercept(enrich(request));
    }

    @Override
    public <T, V> CompletableFuture<TelegramCoreResponse<T>> sendAsync(Request<V> request) {
        return adapter.sendAsync(enrich(request))
                .thenApply(response -> new AggregationTelegramHttpClient(new TelegramHttpClientInterceptorChain() {
                    @Override
                    public <K, L> TelegramCoreResponse<K> intercept(Request<L> request) {
                        return processRequest(request, response);
                    }
                }, interceptors).intercept(request));
    }

    private <V> Request<V> enrich(Request<V> request) {
        if(request.getHost() == null && url != null){
            if(log.isDebugEnabled())
                log.debug("request not contains endpoint host. null value will be replaced by default { default_value = {} }", url);
            request.setHost(url);
        }
        if(request.getToken() == null && token != null){
            if(log.isDebugEnabled())
                log.debug("request not contains bot-token. null value will be replaced by default { default_value = **** (check out configuration, token is masked) }");
            request.setHost(token);
        }
        return request;
    }

    private <T, V> TelegramCoreResponse<T> sync(Request<V> request) {
        return processRequest(request, adapter.send(request));
    }

    private  <T, V> TelegramCoreResponse<T> processRequest(Request<V> request, byte[] response) {
        @SuppressWarnings("unchecked")
        PathHandler<T> handler = (PathHandler<T>) pathHandlers.get(request.getPath());

        if (handler != null) {
            if (log.isDebugEnabled())
                log.debug("found path handler on selected url { path = {}, handler = {} }", request.getHost(), handler.getClass());
        } else {
            handler = new PathHandler<>() {
                @Override
                @SneakyThrows
                public TelegramCoreResponse<T> handle(byte[] response) {
                    return decoder.readValue(response, decoder.getTypeFactory().
                            constructParametricType(TelegramCoreResponse.class, request.getResponseType()));
                }

                @Override
                public String handlingOn() {
                    return "/*";
                }
            };

            if (log.isDebugEnabled())
                log.debug("not found path handler on selected url { path = {}, default_handler = {} }", request.getHost(), handler.getClass());
        }

        return handler.handle(response);
    }

}
