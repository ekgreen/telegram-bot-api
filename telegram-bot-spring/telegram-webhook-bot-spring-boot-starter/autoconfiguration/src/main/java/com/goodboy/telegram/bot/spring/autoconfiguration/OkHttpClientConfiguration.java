package com.goodboy.telegram.bot.spring.autoconfiguration;

import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.http.client.OkHttpClientAdapter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkHttpClientConfiguration {

    @Bean @ConditionalOnMissingBean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .build();
    }

    @Bean @ConditionalOnMissingBean
    public HttpClientAdapter httpClientAdapter(OkHttpClient okHttpClient) {
        return new OkHttpClientAdapter(okHttpClient);
    }

    private static Interceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
