package com.goodboy.telegram.bot.client;

import com.goodboy.telegram.bot.core.client.TelegramHttpResponse;
import com.goodboy.telegram.bot.core.client.uni.UniTypeRequest;
import com.goodboy.telegram.bot.core.client.UniTypedHttpClientAdapter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.MultipartBody.Part;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Setter
@RequiredArgsConstructor
public class OkHttpClientAdapter implements UniTypedHttpClientAdapter {

    private final OkHttpClient client;
    private ExecutorService executorService;

    @Override
    public <V> TelegramHttpResponse post(UniTypeRequest<V> request) {
        return null;
    }

    @Override
    public <V> CompletableFuture<TelegramHttpResponse> postAsync(UniTypeRequest<V> request) {
        return null;
    }

    @Override
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
        return null;
    }

    @Override
    public <V> CompletableFuture<TelegramHttpResponse> multipartAsync(UniTypeRequest<V> request) {
        return null;
    }

    private Request buildMultipartRequest(UniTypeRequest<?> request) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(new Part.Builder)
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
