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

package com.goodboy.telegram.bot.http.api.client.context;

import com.goodboy.telegram.bot.api.BotCommand;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Данные о боте и его окружении вместе с клиентским запросом к сервису
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public interface UpdateContext {

    /**
     * @return идентификатор бота в телеграм
     */
    @Nonnull Long getBotId();

    /**
     * @return название обрабатывающего бота
     */
    @Nonnull String getBotName();

    /**
     * @return токен вызывающего бота
     */
    @Nonnull String getBotToken();

    /**
     * @return список команд
     */
    @Nonnull List<BotCommand> getBotCommands();
}
