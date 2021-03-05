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

package com.goodboy.telegram.bot.http.api.client.token;

import com.goodboy.telegram.bot.http.api.client.context.TelegramApiContextResolver;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Резолвер основанный на получении данных из контекста, то есть эти данные сначала должны появиться в контексте
 * из другого места, например из другого резолвера который завязан на иной источник данных, а только затем мы сможем
 * их получить в данном экземпляре
 *
 * Обозначения:
 * TR - {@link TelegramApiTokenResolver}
 * C - {@link UpdateContext}
 *
 * Схема:
 * TR -> C -> TR {@link ContextBasedTokenResolver}
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class ContextBasedTokenResolver implements TelegramApiTokenResolver {

    private final TelegramApiContextResolver resolver;

    public String get() {
        final var context = resolver.read();

        return Optional.ofNullable(context).map(UpdateContext::getBotToken).orElse(null);
    }

}
