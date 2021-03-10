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

package com.goodboy.telegram.bot.spring.impl.sender;

import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.http.api.client.extended.ExtendedTelegramHttpClient;
import com.goodboy.telegram.bot.spring.api.sender.ApiMethodExecutor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class ExtendedApiSenderExecutor implements ApiMethodExecutor {

    private final ExtendedTelegramHttpClient client;

    @Override
    public void sendApi(@NotNull UpdateContext context, @NotNull Api api) {
        client.sendWithToken(api, context.getBotToken());
    }
}
