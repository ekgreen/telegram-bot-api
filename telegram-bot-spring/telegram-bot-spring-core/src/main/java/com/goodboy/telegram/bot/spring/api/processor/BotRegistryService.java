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

package com.goodboy.telegram.bot.spring.api.processor;

import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.meta.Bot;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public interface BotRegistryService {

    /**
     * Зарегистрировать бота определенного типа
     *
     * @param beanName  имя бина, который отвечает за данного бота
     * @param botType   тип удовлетворяющий требованиям к боту (помеченный аннотацией {@link Bot}
     */
    @Nonnull
    BotData registryBot(String beanName, Class<?> botType);

    /**
     * Получить данные о боте по его имени (по имени бина)
     *
     * @param beanName имя бина
     * @return данные о боте
     */
    Optional<BotData> getBotDataByBeanName(@Nonnull String beanName);

    /**
     * Получить данные о ботых по его типу
     *
     * @param botType тип бота
     * @return данные о боте
     */
    List<BotData> getBotDataByType(@Nonnull Class<?> botType);

    /**
     * Получить данные о всех зарегистрированных ботах
     *
     * @return данные о боте
     */
    List<BotData> getAll();
}
