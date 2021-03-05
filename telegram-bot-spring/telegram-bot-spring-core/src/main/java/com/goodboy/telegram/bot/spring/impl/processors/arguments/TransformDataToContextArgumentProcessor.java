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

package com.goodboy.telegram.bot.spring.impl.processors.arguments;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiUpdateContextImpl;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.processor.arguments.TypeArgumentProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Infrastructure
public class TransformDataToContextArgumentProcessor implements TypeArgumentProcessor<UpdateContext> {

    @Override
    public void setArgument(int position, @Nonnull Class<?> argumentType, @NotNull Object[] arguments, @NotNull BotData botData, @Nullable Update update) {
        arguments[position] = transformDataToContext(botData);
    }

    @Override
    public Class<? extends UpdateContext> supportedType() {
        return UpdateContext.class;
    }

    /**
     * Метод расчитывает параметры в момент обращения к конкретному методу, а не на стадии поднятия контекста
     *
     * @param botData данные известные о боте, в момент поднятия контекста
     * @return контекст
     */
    public static  @Nonnull
    UpdateContext transformDataToContext(@Nonnull BotData botData) {
        return new TelegramApiUpdateContextImpl(botData.getDefinition().getId(), botData.getName(),  botData.getTelegramApiTokenResolver().get())
                .setBotCommands(botData.getCommands());
    }
}
