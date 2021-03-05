/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.example;

 import com.goodboy.telegram.bot.api.User;
 import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
 import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
 import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
 import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
 import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
 import com.goodboy.telegram.bot.http.api.client.request.RequestType;
 import com.goodboy.telegram.bot.http.api.method.me.TelegramMeImpl;
 import com.goodboy.telegram.bot.http.client.OkHttpFilledTelegramHttpClientBuilder;
 import org.junit.jupiter.api.Assertions;
 import org.junit.jupiter.api.Test;

 import javax.annotation.Nonnull;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
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
