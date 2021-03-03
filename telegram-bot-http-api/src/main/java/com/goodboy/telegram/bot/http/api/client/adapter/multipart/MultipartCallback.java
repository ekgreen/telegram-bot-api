package com.goodboy.telegram.bot.http.api.client.adapter.multipart;

import com.goodboy.telegram.bot.http.api.client.adapter.Callback;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapterCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.TelegramApiToAdapterRequestMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod;
import static com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod.MULTIPART;

@RequiredArgsConstructor
public class MultipartCallback implements HttpClientAdapterCallback {

    private final TelegramApiToAdapterRequestMapper<Iterable<MultipartParameter<?>>> mapper;

    @Override
    public <V> Callback callback(@NotNull HttpClientAdapter adapter, @Nonnull String url, @Nullable V payload) {
        return () -> adapter.multipart(new CallableMultipartRequest(url, mapper.transform(payload)));
    }

    @Override
    public HttpMethod method() {
        return MULTIPART;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    private static class CallableMultipartRequest implements MultipartRequest {
        private final String url;
        private final Iterable<MultipartParameter<?>> payload;
    }
}
