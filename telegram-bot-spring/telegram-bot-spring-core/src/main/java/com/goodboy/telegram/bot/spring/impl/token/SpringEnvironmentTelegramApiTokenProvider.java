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

package com.goodboy.telegram.bot.spring.impl.token;

import com.goodboy.telegram.bot.spring.api.context.TelegramApiProviderContext;
import com.goodboy.telegram.bot.spring.api.token.TelegramApiTokenProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public class SpringEnvironmentTelegramApiTokenProvider implements TelegramApiTokenProvider {

    public @Nullable String resolve(@Nonnull String botName, @Nonnull TelegramApiProviderContext context) {
        final var environment = context.getEnvironment();

        return environment.getProperty(format("telegram.%s.token", botName));
    }
}
