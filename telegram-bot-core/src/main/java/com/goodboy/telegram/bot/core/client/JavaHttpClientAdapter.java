package com.goodboy.telegram.bot.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.client.*;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.core.client.uni.UniTypeRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Slf4j
public class JavaHttpClientAdapter implements UniTypedHttpClientAdapter {

    private final Map<Class<?>, HttpBodyPublisher<?>> multipartDataPublishers = new HashMap<>();

    private final HttpClient client;
    private final ObjectMapper decoder;

    public JavaHttpClientAdapter(HttpClient client, ObjectMapper decoder) {
        this.client = client;
        this.decoder = decoder;
        initializeMultipartPublishingApi();
    }

    @Override
    public <V> TelegramHttpResponse post(UniTypeRequest<V> request) {
        return createTelegramHttpResponse(new PostJsonInstance(request.getUrl()).send(request.getRequest()));
    }

    @Override
    public <V> CompletableFuture<TelegramHttpResponse> postAsync(UniTypeRequest<V> request) {
        return createTelegramHttpResponse(new PostJsonInstance(request.getUrl()).sendAsync(request.getRequest()));
    }

    @RequiredArgsConstructor
    private class PostJsonInstance {

        private final String url;

        @SneakyThrows
        public <V> HttpResponse<byte[]> send(@Nonnull Request<V> request) {
            return client.send(buildRequest(request), HttpResponse.BodyHandlers.ofByteArray());
        }

        public <V> CompletableFuture<HttpResponse<byte[]>> sendAsync(@NotNull Request<V> request) {
            return client.sendAsync(buildRequest(request), HttpResponse.BodyHandlers.ofByteArray());
        }

        private <V> HttpRequest buildRequest(@Nonnull Request<V> request) {
            final V body = request.getBody();
            return HttpRequest
                    .newBuilder(URI.create(url))
                    .headers("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(writeValueAsString(body)))
                    .build();
        }

        private <V> String writeValueAsString(V body) {
            try {
                return decoder.writeValueAsString(body);
            } catch (Exception e) {
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, e);
            }
        }
    }


    @Override
    public TelegramHttpResponse get(UniTypeRequest<?> request) {
        return createTelegramHttpResponse(new EmptyRequestInstance(request.getUrl()).send(request.getRequest()));
    }

    @Override
    public CompletableFuture<TelegramHttpResponse> getAsync(UniTypeRequest<?> request) {
        return createTelegramHttpResponse(new EmptyRequestInstance(request.getUrl()).sendAsync(request.getRequest()));
    }

    @RequiredArgsConstructor
    private class EmptyRequestInstance {

        private final String url;

        @SneakyThrows
        public <V> HttpResponse<byte[]> send(@Nonnull Request<V> request)  {
            return client.send(buildRequest(), HttpResponse.BodyHandlers.ofByteArray()
            );
        }

        public <V> CompletableFuture<HttpResponse<byte[]>> sendAsync(@NotNull Request<V> request) {
            return client.sendAsync(buildRequest(), HttpResponse.BodyHandlers.ofByteArray());
        }

        private HttpRequest buildRequest() {
            return HttpRequest
                    .newBuilder(URI.create(url))
                    .GET()
                    .build();
        }
    }

    @Override
    public <V> TelegramHttpResponse multipart(UniTypeRequest<V> request) {
        return createTelegramHttpResponse(new MultipartInstance(request.getUrl()).send(request.getRequest()));
    }

    @Override
    public <V> CompletableFuture<TelegramHttpResponse> multipartAsync(UniTypeRequest<V> request) {
        return createTelegramHttpResponse(new MultipartInstance(request.getUrl()).sendAsync(request.getRequest()));
    }

    @RequiredArgsConstructor
    private class MultipartInstance{

        private final String url;

        @SneakyThrows
        public <V> HttpResponse<byte[]> send(@Nonnull Request<V> request) {
            return client.send(buildRequest(request), HttpResponse.BodyHandlers.ofByteArray()
            );
        }

        public <V> CompletableFuture<HttpResponse<byte[]>> sendAsync(@NotNull Request<V> request) {
            return client.sendAsync(buildRequest(request), HttpResponse.BodyHandlers.ofByteArray());
        }

        private <V> HttpRequest buildRequest(@Nonnull Request<V> request) {
            final HttpRequest.BodyPublisher multipart = multipart(request.getBody());

            return HttpRequest
                    .newBuilder(URI.create(url))
                    .headers("Content-Type", "multipart/form-data")
                    .POST(multipart)
                    .build();
        }

        private <V> HttpRequest.BodyPublisher multipart(V body) {
            final Class<?> multipartDataType = Objects.requireNonNull(body).getClass();

            if (multipartDataPublishers.containsKey(multipartDataType))
                return (multipartDataPublishers.get(multipartDataType)).instance(body);

            if (String.class.isAssignableFrom(multipartDataType))
                return BodyPublishers.ofString((String) body);

            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "not consistent code, multipart(V) where V bounded by: binary or string format");
        }
    }

    private TelegramHttpResponse createTelegramHttpResponse(HttpResponse<byte[]> response) {
        return new TelegramHttpResponse()
                .setStatusCode(response.statusCode())
                .setBody(response.body());
    }

    private CompletableFuture<TelegramHttpResponse> createTelegramHttpResponse(CompletableFuture<HttpResponse<byte[]>> future) {
        return future.thenApply(this::createTelegramHttpResponse);
    }

    private void initializeMultipartPublishingApi() {
        registryPublisher(Byte[].class, bytes -> {
            log.warn("missed Byte[].class body publisher in jdk (only byte[]), use byte[]/ByteBuffer to wrap byte arrays");
            return BodyPublishers.ofByteArray(ArrayUtils.toPrimitive(bytes));
        });
        registryPublisher(byte[].class, BodyPublishers::ofByteArray);
        registryPublisher(ByteBuffer.class, buffer -> BodyPublishers.ofByteArray(buffer.array()));
        registryPublisher(Path.class, path -> {
            try {
                return BodyPublishers.ofFile(path);
            } catch (FileNotFoundException exception) {
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, exception);
            }
        });
        registryPublisher(File.class, file -> BodyPublishers.ofInputStream(() -> {
            try {
                log.warn("missed File.class body publisher in jdk, use Path/InputStream to receiving files. changed on InputStream publisher");
                return new DataInputStream(new FileInputStream(file));
            } catch (FileNotFoundException exception) {
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, exception);
            }
        }));
        // probably missed associations if child's
        registryPublisher(InputStream.class, stream -> BodyPublishers.ofInputStream(() -> stream));
    }

    private <V> void registryPublisher(@Nonnull Class<V> type, @Nonnull Function<V, HttpRequest.BodyPublisher> publisher) {
        registryPublisher(type, new HttpBodyPublisher<>() {
            @Override
            @SuppressWarnings("unchecked")
            public HttpRequest.BodyPublisher instance(Object body) {
                return publisher.apply((V) body);
            }

            @Override
            public Class<V> publishingBodyType() {
                return type;
            }
        });
    }
    private <V> void registryPublisher(@Nonnull Class<V> type, @Nonnull HttpBodyPublisher<V> publisher) {
        if (log.isDebugEnabled())
            log.debug("registry body publisher for java http client { consumes = {}, publisher_producer = {} }", type, publisher.getClass());

        this.multipartDataPublishers.put(type, publisher);
    }

    public Set<Class<?>> getMultipartTypePublishers() {
        return this.multipartDataPublishers.keySet();
    }
}
