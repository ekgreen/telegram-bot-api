package com.goodboy.telegram.bot.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.api.client.adapter.MultipartHttpClientHandler;
import com.goodboy.telegram.bot.api.client.adapter.UniTypeRequest;
import com.goodboy.telegram.bot.api.response.TelegramHttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Setter
@RequiredArgsConstructor
public class OkHttpClientAdapter implements HttpClientAdapter {

    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final MultipartHttpClientHandler multipartHttpClientHandler;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public <V> TelegramHttpResponse post(UniTypeRequest<V> request) {
        return handleRequest(buildPostRequest(request));
    }

    public <V> CompletableFuture<TelegramHttpResponse> postAsync(UniTypeRequest<V> request) {
        return handleAsyncRequest(buildPostRequest(request));
    }

    @SneakyThrows
    private Request buildPostRequest(UniTypeRequest<?> request) {
        return new Request.Builder()
                .url(request.getUrl())
                .post(RequestBody.create(mapper.writeValueAsString(request.getRequest().getBody()), JSON))
                .build();
    }

    public TelegramHttpResponse get(UniTypeRequest<?> request) {
        return handleRequest(buildGetRequest(request));
    }

    @Override
    public CompletableFuture<TelegramHttpResponse> getAsync(UniTypeRequest<?> request) {
        return handleAsyncRequest(buildGetRequest(request));
    }

    private Request buildGetRequest(UniTypeRequest<?> request) {
        return new Request.Builder()
                .url(request.getUrl())
                .build();
    }

    @Override
    public <V> TelegramHttpResponse multipart(UniTypeRequest<V> request) {
        return handleRequest(buildMultipartRequest(request));
    }

    @Override
    public <V> CompletableFuture<TelegramHttpResponse> multipartAsync(UniTypeRequest<V> request) {
        return handleAsyncRequest(buildMultipartRequest(request));
    }

    private Request buildMultipartRequest(UniTypeRequest<?> request) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        multipartHttpClientHandler.parts(request.getRequest()).forEach(part -> {
            if(part instanceof MultipartHttpClientHandler.StringPart){
                MultipartHttpClientHandler.StringPart string = (MultipartHttpClientHandler.StringPart) part;

                builder.addFormDataPart(part.getKey(), string.getHandler());
            } else if(part instanceof MultipartHttpClientHandler.StreamPart){
                MultipartHttpClientHandler.StreamPart stream = (MultipartHttpClientHandler.StreamPart) part;

                builder.addFormDataPart(part.getKey(), stream.getFileName(), RequestBody.create(stream.getHandler().get()));
            } else
                throw new IllegalStateException("illegal uploading class = " + part.getClass());
        });

        return new Request.Builder()
                .url(request.getUrl())
                .post(builder.build())
                .build();
    }

    @SneakyThrows
    private TelegramHttpResponse handleRequest(@Nonnull Request request) {
        try(Response response = client.newCall(request).execute()){
            return convert(response);
        }
    }

    private CompletableFuture<TelegramHttpResponse> handleAsyncRequest(@Nonnull Request request) {
        final CompletableFuture<TelegramHttpResponse> future = new CompletableFuture<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                future.complete(convert(response));
            }
        });

        return future;
    }

    @SneakyThrows
    private TelegramHttpResponse convert(@Nonnull Response response) {
        return new TelegramHttpResponse()
                .setStatusCode(response.code())
                .setBody(Objects.requireNonNull(response.body()).bytes());
    }
}
