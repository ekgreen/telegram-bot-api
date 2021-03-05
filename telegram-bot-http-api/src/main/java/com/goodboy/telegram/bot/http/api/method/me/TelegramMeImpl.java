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

package com.goodboy.telegram.bot.http.api.method.me;

import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.api.methods.me.TelegramMeApi;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class TelegramMeImpl implements TelegramMeApi {

    private final TelegramHttpClient client;

    @Override
    public @NotNull TelegramCoreResponse<User> getMe() {
        return client.send(new MethodRequest<>(
                        new CallMethodImpl(TelegramMethodApiDefinition.GET_ME, RequestType.COMMAND, User.class)
                                .setHttpMethod(Request.HttpMethod.GET)
                )
        );
    }
}
