package com.goodboy.telegram.bot.http.api.client.adapter.get;

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
import static com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod.GET;

@RequiredArgsConstructor
public class GetCallback implements HttpClientAdapterCallback {

    private final TelegramApiToAdapterRequestMapper<Iterable<QueryAttribute>> mapper;

    @Override
    public <V> Callback callback(@NotNull HttpClientAdapter adapter, @Nonnull String url, @Nullable V payload) {
        return () -> adapter.get(new CallableGetRequest(url, mapper.transform(payload)));
    }

    @Override
    public HttpMethod method() {
        return GET;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    private static class CallableGetRequest implements GetRequest {
        private final String url;
        private final Iterable<QueryAttribute> payload;
    }
}
