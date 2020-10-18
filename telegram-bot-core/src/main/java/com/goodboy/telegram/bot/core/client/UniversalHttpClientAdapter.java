package com.goodboy.telegram.bot.core.client;

import com.goodboy.telegram.bot.api.client.*;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.core.client.uni.UniTypeRequest;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Setter
@RequiredArgsConstructor
public class UniversalHttpClientAdapter implements CustomizableHttpClientAdapter {

    /**
     * Default endpoint is <a href="https://api.telegram.org/">telegram</a>
     * Will replaced by null host value
     */
    private final static String DEFAULT_HOST = "https://api.telegram.org";

    private final UniTypedHttpClientAdapter client;

    private final Set<Class<?>> multipartHandledClass = Sets.newHashSet();

    // client properties
    private boolean throwExceptionOnNonOkResponse = true;

    @Override
    public <V> byte[] send(@Nonnull Request<V> request) throws TelegramApiRuntimeException {
        return getOriginClientByRequestGenericType(request).send(request);
    }

    @Override
    public <V> CompletableFuture<byte[]> sendAsync(Request<V> request) {
        return getOriginClientByRequestGenericType(request).sendAsync(request);
    }

    @Override
    public CustomizableHttpClientAdapter multipartHandledClass(Class<?> type) {
        this.multipartHandledClass.add(type);
        return this;
    }

    @Override
    public CustomizableHttpClientAdapter throwExceptionOnNonOkResponse(boolean throwExceptionOnNonOkResponse) {
        this.throwExceptionOnNonOkResponse = throwExceptionOnNonOkResponse;
        return this;
    }

    private <V> HttpClientAdapter getOriginClientByRequestGenericType(Request<V> request) {
        InnerHttpClientAdapter adapter;

        final String url = createUrl(request);

        @Nullable V body = request.getBody();

        if (isNullOrEmptyBody(body)) {
            // 1) if request contains no body -> GetRequestInstance
            adapter = new InnerHttpClientAdapter() {
                public <S> TelegramHttpResponse send(@NotNull Request<S> request) {
                    return client.get(createUniTypeRequest(url, request));
                }

                public <S> CompletableFuture<TelegramHttpResponse> sendAsync(@NotNull Request<S> request) {
                    return client.getAsync(createUniTypeRequest(url, request));
                }
            };
            if (log.isDebugEnabled())
                log.debug("body (Request.body) is null or empty -> selected client is {}", adapter.getClass());
        } else if (multipartHandledClass.contains(body.getClass())) {
            // 2) if request contains binary array or any byte array wrapper or any string except already json formatted
            adapter = new InnerHttpClientAdapter() {
                public <S> TelegramHttpResponse send(@NotNull Request<S> request) {
                    return client.multipart(createUniTypeRequest(url, request));
                }

                public <S> CompletableFuture<TelegramHttpResponse> sendAsync(@NotNull Request<S> request) {
                    return client.multipartAsync(createUniTypeRequest(url, request));
                }
            };
            if (log.isDebugEnabled())
                log.debug("body (Request.body) have binary or assignable from binary or text format -> selected client is {}", adapter.getClass());
        } else {
            // 3) other cases going under simple post json decoder
            adapter = new InnerHttpClientAdapter() {
                public <S> TelegramHttpResponse send(@NotNull Request<S> request) {
                    return client.post(createUniTypeRequest(url, request));
                }

                public <S> CompletableFuture<TelegramHttpResponse> sendAsync(@NotNull Request<S> request) {
                    return client.postAsync(createUniTypeRequest(url, request));
                }
            };
            if (log.isDebugEnabled())
                log.debug("body (Request.body) have simple post format. body will be converted in json -> selected client is {}", adapter.getClass());
        }

        return new HandleHttpResponse(adapter);
    }

    private <S> UniTypeRequest<S> createUniTypeRequest(String url, Request<S> request) {
        return new UniTypeRequest<S>()
                .setUrl(url)
                .setRequest(request);
    }

    private <V> boolean isNullOrEmptyBody(V body) {
        return body == null
                || (String.class.isAssignableFrom(body.getClass()) && StringUtils.isBlank((CharSequence) body));
    }

    private <V> String createUrl(Request<V> request) {
        @Nullable String host = request.getHost();

        if (host == null) {
            if (log.isDebugEnabled())
                log.debug("host (Request.host) value is null. replaced by default value { default_host = {} }", DEFAULT_HOST);
            host = DEFAULT_HOST;
        }

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

    private interface InnerHttpClientAdapter{
        <S> TelegramHttpResponse send(@Nonnull Request<S> request);

        <S> CompletableFuture<TelegramHttpResponse> sendAsync(@Nonnull Request<S> request);
    }

    @RequiredArgsConstructor
    private class HandleHttpResponse implements HttpClientAdapter {

        private final InnerHttpClientAdapter adapter;

        @Override
        public <V> byte[] send(@NotNull Request<V> request) {
            try {
                return handleHttpResponse(adapter.send(request));
            } catch (Exception e) {
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_RESPONSE_ERROR, e);
            }
        }

        @Override
        public <V> CompletableFuture<byte[]> sendAsync(Request<V> request) {
            return adapter.sendAsync(request)
                    .thenApply(this::handleHttpResponse);
        }

        private byte[] handleHttpResponse(TelegramHttpResponse response) {
            int code = response.getStatusCode();

            if (code == 200)
                return response.getBody();

            if (UniversalHttpClientAdapter.this.throwExceptionOnNonOkResponse)
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.NOT_200_HTTP_STATUS_CODE, "status code " + code);

            return null;
        }
    }

}
