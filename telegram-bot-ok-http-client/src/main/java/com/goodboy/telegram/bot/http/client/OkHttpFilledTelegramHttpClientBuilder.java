package com.goodboy.telegram.bot.http.client;

import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.builder.FilledTelegramHttpClientBuilder;
import com.google.common.base.Suppliers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

public class OkHttpFilledTelegramHttpClientBuilder extends FilledTelegramHttpClientBuilder {

    // default ok http client - single instance on all calls
    private final static Supplier<OkHttpClient> OK_HTTP_CLIENT_FACTORY = Suppliers.memoize(OkHttpFilledTelegramHttpClientBuilder::buildOkHttpClient);

    private OkHttpClient client;

    public OkHttpFilledTelegramHttpClientBuilder client(@NotNull OkHttpClient client) {
        Objects.requireNonNull(client, "client cannot be null");
        this.client = client;
        return this;
    }

    public TelegramHttpClient build(){
        return build(new OkHttpClientAdapter(
                buildClient()
        ));
    }

    private @Nonnull OkHttpClient buildClient() {
        return client != null ? client : OK_HTTP_CLIENT_FACTORY.get();
    }

    private static OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .build();
    }

    private static Interceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
