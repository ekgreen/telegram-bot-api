package com.goodboy.telegram.bot.core.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.client.HttpBodyPublisher;
import com.goodboy.telegram.bot.api.client.HttpClientAdapter;
import com.goodboy.telegram.bot.api.client.JavaHttpClient;
import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Slf4j
@Setter
public class JavaHttpClientAdapter implements JavaHttpClient {

    /**
     * Default endpoint is <a href="https://api.telegram.org/">telegram</a>
     * Will replaced by null host value
     */
    private final static String DEFAULT_HOST = "https://api.telegram.org";

    private final java.net.http.HttpClient client;
    private final ObjectMapper decoder;

    // optional fields
    private final Map<Class<?>, HttpBodyPublisher<?>> multipartDataPublishers = new HashMap<>();
    // client properties
    private boolean intensiveJsonValidation = true;
    private boolean postEmptyRequests = false;
    private boolean throwExceptionOnNonOkResponse = true;

    public JavaHttpClientAdapter(java.net.http.HttpClient client, ObjectMapper decoder) {
        this.client = client;
        this.decoder = decoder;
        initializeMultipartPublishingApi();
    }

    @Override
    public <V> byte[] send(@Nonnull Request<V> request) throws TelegramApiRuntimeException {
        return getOriginClientByRequestGenericType(request).send(request);
    }

    @Override
    public <V> CompletableFuture<byte[]> sendAsync(Request<V> request) {
        return getOriginClientByRequestGenericType(request).sendAsync(request);
    }

    @Override
    public <V> JavaHttpClient registryPublisher(HttpBodyPublisher<V> publisher) {
        // default values will be vanished on any collision
        registryPublisher(publisher.publishingBodyType(), publisher);
        return this;
    }

    @Override
    public JavaHttpClient intensiveJsonValidation(boolean sign) {
        this.intensiveJsonValidation = sign;
        return this;
    }

    @Override
    public JavaHttpClient usePostForEmptyRequests(boolean usePostMethodForEmptyRequests) {
        this.postEmptyRequests = usePostMethodForEmptyRequests;
        return this;
    }

    @Override
    public JavaHttpClient throwExceptionOnNonOkResponse(boolean throwExceptionOnNonOkResponse) {
        this.throwExceptionOnNonOkResponse = throwExceptionOnNonOkResponse;
        return this;
    }

    private <V> HttpClientAdapter getOriginClientByRequestGenericType(Request<V> request) {
        final InnerHttpClientAdapter adapter;

        final String url = createUrl(request);
        boolean isJson = false;
        boolean isBinary;

        @Nullable V body = request.getBody();

        if (isNullOrEmptyBody(body)) {
            // 1) if request contains no body -> GetRequestInstance
            adapter = new EmptyRequestInstance(url);
            if (log.isDebugEnabled())
                log.debug("body (Request.body) is null or empty -> selected client is {}", adapter.getClass());
        } else if (isBinary = isBinaryBody(request) || !(isJson = (isJson(request)))) {
            // 2) if request contains binary array or any byte array wrapper or any string except already json formatted
            adapter = new MultipartInstance(url, isBinary);
            if (log.isDebugEnabled())
                log.debug("body (Request.body) have binary or assignable from binary or text format -> selected client is {}", adapter.getClass());
        } else {
            // 3) other cases going under simple post json decoder
            adapter = new PostJsonInstance(url, isJson);
            if (log.isDebugEnabled())
                log.debug("body (Request.body) have simple post format. body will be converted in json -> selected client is {}", adapter.getClass());
        }

        return new HandleHttpResponse(adapter);
    }

    private <V> boolean isBinaryBody(@Nonnull Request<V> request) {
        return multipartDataPublishers.containsKey(request.getBody().getClass());
    }

    private <T> boolean isJson(@Nonnull Request<T> request) {
        if (!String.class.isAssignableFrom(request.getBody().getClass()))
            return false;

        @Nonnull String body = (String) request.getBody();

        char firstLetter = body.charAt(0);
        char lastLetter = body.charAt(body.length() - 1);

        if ((firstLetter == '{' && lastLetter == '}') || (firstLetter == '[' && lastLetter == ']')) {
            final Set<Character> wanted = Sets.newHashSet('{', '}', '[', ']', '"');
            // 1) skipped some semantics and check correct json frame
            final Deque<Character> stack = new ArrayDeque<>();

            // region simple json validation
            for (int p = 0; p < body.length(); p++) {
                char character = body.charAt(p);

                // json file contains a lot of whitespaces and tabulations skip it early than check in wanted set
                if (character == ' ' || character == '\t')
                    break;

                if (wanted.contains(character)) {
                    Character head;
                    switch (character) {
                        // opening brackets
                        case '{':
                        case '[':
                            stack.push(character);
                            break;
                        case '"':
                            if (stack.isEmpty())
                                return false;

                            head = stack.peek();
                            if (head != null && head == character) {
                                stack.poll();
                            } else {
                                stack.push(character);
                            }
                            break;
                        // closing brackets
                        case '}':
                            if (stack.isEmpty())
                                return false;

                            head = stack.peek();

                            if (head != '{')
                                return false;
                            else
                                stack.poll();

                            break;
                        case ']':
                            if (stack.isEmpty())
                                return false;

                            head = stack.peek();

                            if (head != '[')
                                return false;
                            else
                                stack.poll();
                            break;
                    }
                }
            }
            // endregion

            if (intensiveJsonValidation) {
                // 2) intensive check via third party library -> build json tree
                try {
                    decoder.readTree(body);
                    return true;
                } catch (JsonProcessingException exception) {
                    /* exception not matter */
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
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
                + "bot" + Objects.requireNonNull(request.getAuthToken(), "missed required filed (Request.authToken). use @BotFather to create it")
                + "/"
                + Objects.requireNonNull(request.getCallName(), "missed required filed (Request.callName). " +
                "check telegram documentation and declare calling method name");

        if (log.isDebugEnabled())
            log.debug("created http request's url { url = {} }", url);

        return url;
    }

    private interface InnerHttpClientAdapter {
        <V> HttpResponse<byte[]> send(@Nonnull Request<V> request) throws Exception;

        <V> CompletableFuture<HttpResponse<byte[]>> sendAsync(@Nonnull Request<V> request);
    }

    @RequiredArgsConstructor
    private class MultipartInstance implements InnerHttpClientAdapter {

        private final String url;
        private final boolean isBinary;

        @Override
        public <V> HttpResponse<byte[]> send(@Nonnull Request<V> request) throws IOException, InterruptedException {
            return client.send(buildRequest(request), HttpResponse.BodyHandlers.ofByteArray()
            );
        }

        @Override
        public <V> CompletableFuture<HttpResponse<byte[]>> sendAsync(@NotNull Request<V> request) {
            return client.sendAsync(buildRequest(request), HttpResponse.BodyHandlers.ofByteArray());
        }

        private <V> HttpRequest buildRequest(@Nonnull Request<V> request) {
            final BodyPublisher multipart = multipart(request.getBody());

            return HttpRequest
                    .newBuilder(URI.create(url))
                    .headers("Content-Type", "multipart/form-data")
                    .POST(multipart)
                    .build();
        }

        private <V> BodyPublisher multipart(V body) {
            final Class<?> multipartDataType = Objects.requireNonNull(body).getClass();

            if (isBinary)
                return (multipartDataPublishers.get(multipartDataType)).instance(body);

            if (String.class.isAssignableFrom(multipartDataType))
                return BodyPublishers.ofString((String) body);

            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "not consistent code, multipart(V) where V bounded by: binary or string format");
        }
    }

    @RequiredArgsConstructor
    private class PostJsonInstance implements InnerHttpClientAdapter {

        private final String url;
        private final boolean isJson;

        @Override
        public <V> HttpResponse<byte[]> send(@Nonnull Request<V> request) throws IOException, InterruptedException {
            return client.send(buildRequest(request), HttpResponse.BodyHandlers.ofByteArray());
        }

        @Override
        public <V> CompletableFuture<HttpResponse<byte[]>> sendAsync(@NotNull Request<V> request) {
            return client.sendAsync(buildRequest(request), HttpResponse.BodyHandlers.ofByteArray());
        }

        private <V> HttpRequest buildRequest(@Nonnull Request<V> request) {
            final V body = request.getBody();
            return HttpRequest
                    .newBuilder(URI.create(url))
                    .headers("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(isJson ? (String) body : writeValueAsString(body)))
                    .build();
        }
    }

    @RequiredArgsConstructor
    private class EmptyRequestInstance implements InnerHttpClientAdapter {

        private final String url;

        @Override
        public <V> HttpResponse<byte[]> send(@Nonnull Request<V> request) throws IOException, InterruptedException {
            return client.send(buildRequest(), HttpResponse.BodyHandlers.ofByteArray()
            );
        }

        @Override
        public <V> CompletableFuture<HttpResponse<byte[]>> sendAsync(@NotNull Request<V> request) {
            return client.sendAsync(buildRequest(), HttpResponse.BodyHandlers.ofByteArray());
        }

        private HttpRequest buildRequest() {
            return postEmptyRequests ?
                    HttpRequest
                            .newBuilder(URI.create(url))
                            .POST(BodyPublishers.noBody())
                            .build() :
                    HttpRequest
                            .newBuilder(URI.create(url))
                            .GET()
                            .build();
        }
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

        private byte[] handleHttpResponse(HttpResponse<byte[]> response) {
            int code = response.statusCode();

            if (code == 200)
                return response.body();

            if (JavaHttpClientAdapter.this.throwExceptionOnNonOkResponse)
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.NOT_200_HTTP_STATUS_CODE, "status code " + code);

            return null;
        }
    }

    private <V> String writeValueAsString(V body) {
        try {
            return decoder.writeValueAsString(body);
        } catch (Exception e) {
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, e);
        }
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


    private <V> void registryPublisher(@Nonnull Class<V> type, @Nonnull Function<V, BodyPublisher> publisher) {
        registryPublisher(type, new HttpBodyPublisher<>() {
            @Override
            @SuppressWarnings("unchecked")
            public BodyPublisher instance(Object body) {
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
}
