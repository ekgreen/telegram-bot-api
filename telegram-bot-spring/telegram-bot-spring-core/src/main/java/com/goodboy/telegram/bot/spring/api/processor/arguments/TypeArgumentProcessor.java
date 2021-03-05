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

package com.goodboy.telegram.bot.spring.api.processor.arguments;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.data.BotData;

import javax.annotation.Nonnull;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public interface TypeArgumentProcessor<C> {

    /**
     * Проставляем аргумент определенного типа
     * На вход в метод передаются основные компоненты, которые могу повлиять на динамизм проставления аргументов, а именно
     * данные о боте {@link BotData} + информация о запросе {@link Update}
     *
     * @param position      позиция в которой находится аргумент
     * @param argumentType  предполагаемы тип аргумента
     * @param arguments     перечень всех аргументов метода
     * @param botData       данные о боте
     * @param update        данные о запросе
     */
    void setArgument(int position, @Nonnull Class<?> argumentType, @Nonnull Object[] arguments, @Nonnull BotData botData, Update update);

    /**
     * Поддерживаемый тип аргумента для данного процессора
     *
     * @return тип
     */
    Class<? extends C> supportedType();

}
