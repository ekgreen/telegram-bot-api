package com.goodboy.telegram.bot.example;

import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.builder.FilledTelegramHttpClientBuilder;
import com.goodboy.telegram.bot.http.api.client.builder.TelegramHttpClientBuilder;
import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.http.api.method.me.TelegramMeImpl;
import com.goodboy.telegram.bot.http.client.OkHttpFilledTelegramHttpClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

public final class DifferentApiCallExamples {

    @Test
    public void sendMessageWithApi() {
        // simple call

        final TelegramHttpClient client = new OkHttpFilledTelegramHttpClientBuilder()
                .token(getToken())
                .build();

        // create getMe service
        // use api is most simple way to call telegram bot api - api hides from user inner implementation
        // of telegram bot http client
        final var api = new TelegramMeImpl(client);

        // request to rich info about bot
        // all responses wraps in uniform telegram api response
        TelegramCoreResponse<User> response = api.getMe();

        // assert that it is success call
        Assertions.assertTrue(response.isOk());
        // my test bot name is 10words - here no logic
        Assertions.assertEquals("10words", response.getResult().getFirstName());
    }


    @Test
    public void sendMessageWithClientTest() {
        // this example show some entrails of client implementation
        final TelegramHttpClient client = new OkHttpFilledTelegramHttpClientBuilder()
                .token(getToken())
                .build();


        // request to rich info about bot
        // all responses wraps in uniform telegram api response
        final TelegramCoreResponse<User> response = client.send(
                // method request is description of calling method with body and environment such as token
                // NOTE! that we already created client implementation tied to direct bot and it is not necessary
                // provide token on each call
                new MethodRequest<>(
                        // technical calling method description
                        // - calling telegram api name
                        // - type of calling api
                        // - expected returning type
                        new CallMethodImpl(TelegramMethodApiDefinition.GET_ME, RequestType.COMMAND, User.class)
                )
        );

        // assert that it is success call
        Assertions.assertTrue(response.isOk());
        // my test bot name is 10words - here no logic
        Assertions.assertEquals("10words", response.getResult().getFirstName());
    }

    public @Nonnull String getToken() {
        // always hide your token from other!
        return System.getenv("test.token.value");
    }
}
