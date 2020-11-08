package com.goodboy.telegram.bot.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.client.*;
import com.goodboy.telegram.bot.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.api.client.adapter.UniTypeRequest;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.api.meta.Multipart;
import com.goodboy.telegram.bot.api.method.token.TokenSupplier;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.api.response.TelegramHttpResponse;
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
import java.util.Objects;
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
    private final TelegramHttpClientProperties properties;
    private final Map<String, PathHandler<?>> pathHandlers;
    private final List<TelegramHttpClientInterceptor> interceptors;
    private final TokenSupplier tokenSupplier;
    private final String host;

    public TelegramHttpClientImpl(
            HttpClientAdapter adapter,
            ObjectMapper decoder
    ){
        this(adapter, decoder, null, (List<PathHandler<?>>) null, null, null, DEFAULT_HOST);
    }

    public TelegramHttpClientImpl(
            HttpClientAdapter adapter,
            ObjectMapper decoder,
            TelegramHttpClientProperties properties,
            @Nullable List<PathHandler<?>> pathHandlers,
            @Nullable List<TelegramHttpClientInterceptor> interceptors,
            @Nullable TokenSupplier tokenSupplier,
            @Nullable String url
    ) {
        this.adapter = adapter;
        this.decoder = decoder;
        this.properties = properties != null ? properties : new TelegramHttpClientProperties();
        this.pathHandlers = pathHandlers != null ? pathHandlers.stream().collect(toMap(
                PathHandler::handlingOn,
                handler -> handler
        )) : ImmutableMap.of();
        this.interceptors = interceptors != null ? interceptors : ImmutableList.of();
        this.tokenSupplier = tokenSupplier != null ? tokenSupplier : () -> null;
        this.host = StringUtils.isNotBlank(url) ? url : DEFAULT_HOST;
    }

    @Override
    public <T, V> TelegramCoreResponse<T> send(@NotNull Request<V> request) {
        return new AggregationTelegramHttpClient(getOriginClientByRequestGenericType(request)::send, interceptors).intercept(request);
    }

    @Override
    public <T, V> CompletableFuture<TelegramCoreResponse<T>> sendAsync(Request<V> request) {
        return getOriginClientByRequestGenericType(request).sendAsync(request);
    }

    private <T, V> TelegramHttpClient getOriginClientByRequestGenericType(Request<V> request) {
        InnerHttpClientAdapter adapter;

        final Request.RequestType requestType = request.getRequestType();
        final String url = createUrl(request);

        @Nullable V body = request.getBody();

        if(requestType == Request.RequestType.GET || (requestType == Request.RequestType.AUTO && isNullOrEmptyBody(body))){
            adapter = new InnerHttpClientAdapter() {

                @Override
                public <A> TelegramHttpResponse send(@NotNull Request<A> request) {
                    return TelegramHttpClientImpl.this.adapter.get(createRequest(url, request));
                }

                @Override
                public <A> CompletableFuture<TelegramHttpResponse> sendAsync(@NotNull Request<A> request) {
                    return TelegramHttpClientImpl.this.adapter.getAsync(createRequest(url, request));
                }

            };
            if (log.isDebugEnabled())
                log.debug("body (Request.body) is null or empty -> selected client is GET");
        }else if(requestType == Request.RequestType.MULTIPART || (requestType == Request.RequestType.AUTO && isMultipartBody(body))){
            adapter = new InnerHttpClientAdapter() {

                @Override
                public <A> TelegramHttpResponse send(@NotNull Request<A> request) {
                    return TelegramHttpClientImpl.this.adapter.multipart(createRequest(url, request));
                }

                @Override
                public <A> CompletableFuture<TelegramHttpResponse> sendAsync(@NotNull Request<A> request) {
                    return TelegramHttpClientImpl.this.adapter.multipartAsync(createRequest(url, request));
                }
            };
            if (log.isDebugEnabled())
                log.debug("body (Request.body) is null or empty -> selected client is MULTIPART");
        } else {
            adapter = new InnerHttpClientAdapter() {

                @Override
                public <A> TelegramHttpResponse send(@NotNull Request<A> request) {
                    return TelegramHttpClientImpl.this.adapter.post(createRequest(url, request));
                }

                @Override
                public <A> CompletableFuture<TelegramHttpResponse> sendAsync(@NotNull Request<A> request) {
                    return TelegramHttpClientImpl.this.adapter.postAsync(createRequest(url, request));
                }
            };
            if (log.isDebugEnabled())
                log.debug("body (Request.body) is null or empty -> selected client is POST");
        }

        return new HandleHttpResponse(adapter);
    }


    private interface InnerHttpClientAdapter{
        <A> TelegramHttpResponse send(@NotNull Request<A> request);

        <A> CompletableFuture<TelegramHttpResponse> sendAsync(@NotNull Request<A> request);
    }

    private <A> UniTypeRequest<A> createRequest(@NotNull String url, @NotNull Request<A> request) {
        return new UniTypeRequest<A>()
                .setUrl(url)
                .setRequest(request);
    }

    private <V> boolean isNullOrEmptyBody(V body) {
        return body == null
                || (String.class.isAssignableFrom(body.getClass()) && StringUtils.isBlank((CharSequence) body));
    }

    private <V> boolean isMultipartBody(@NotNull V body) {
        return body.getClass().isAnnotationPresent(Multipart.class);
    }

    private <V> String createUrl(Request<V> request) {
        final String url = host
                + (request.getPath() != null ? request.getPath() : "/")
                + "bot" + Objects.requireNonNull(request.getToken(), "missed required filed (Request.authToken). use @BotFather to create it")
                + "/"
                + Objects.requireNonNull(request.getCallName(), "missed required filed (Request.callName). " +
                "check telegram documentation and declare calling method name");

        if (log.isDebugEnabled())
            log.debug("created http request's url { url = {} }", url);

        return url;
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

    @RequiredArgsConstructor
    private class HandleHttpResponse implements TelegramHttpClient {

        private final InnerHttpClientAdapter adapter;

        @Override
        public <T, V> TelegramCoreResponse<T> send(@NotNull Request<V> request) {
            TelegramHttpResponse response = adapter.send(request);

            byte[] bytes = handleHttpResponse(response);

            return processRequest(request, bytes);
        }

        @Override
        public <T, V> CompletableFuture<TelegramCoreResponse<T>> sendAsync(Request<V> request) {
            return adapter.sendAsync(request)
                    .thenApply(this::handleHttpResponse)
                    .thenApply(data -> processRequest(request, data));
        }

        private byte[] handleHttpResponse(TelegramHttpResponse response) {
            int code = response.getStatusCode();

            if (code == 200)
                return response.getBody();

            if (properties.isThrowExceptionOnNonOkResponse()) {
                if(log.isDebugEnabled() && response.getBody() != null)
                    log.debug("request for telegram api failed with message:" + new String(response.getBody()));
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.NOT_200_HTTP_STATUS_CODE, "status code " + code);
            }

            return null;
        }

        private  <T, V> TelegramCoreResponse<T> processRequest(Request<V> request, byte[] response) {
            @SuppressWarnings("unchecked")
            PathHandler<T> handler = (PathHandler<T>) pathHandlers.get(request.getPath());

            if (handler != null) {
                if (log.isDebugEnabled())
                    log.debug("found path handler on selected url { path = {}, handler = {} }", host, handler.getClass());
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
                    log.debug("not found path handler on selected url { path = {}, default_handler = {} }", host, handler.getClass());
            }

            return handler.handle(response);
        }

    }



}
