package com.goodboy.telegram.bot.example;

import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.extended.ExtendedTelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.extended.PathScanningExtendedTelegramClient;
import com.goodboy.telegram.bot.http.client.OkHttpFilledTelegramHttpClientBuilder;
import com.google.common.base.Suppliers;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface BotTest {

    // get token and cache
    public final static Supplier<String> token = Suppliers.memoize(BotTest::getToken);

    default public TelegramHttpClient client(){
        return new OkHttpFilledTelegramHttpClientBuilder()
                .token(token.get())
                .build();
    }

    default public ExtendedTelegramHttpClient extendedClient(){
        return new PathScanningExtendedTelegramClient(client());
    };

    public static @Nonnull String getToken() {
        // always hide your token from other!
        return System.getenv("DEFAULT_TEST_TOKEN_VALUE");
    }

    public static @Nonnull String getExampleChatId() {
        // always hide all :D
        return System.getenv("EXAMPLE_CHAT_ID");
    }
}
