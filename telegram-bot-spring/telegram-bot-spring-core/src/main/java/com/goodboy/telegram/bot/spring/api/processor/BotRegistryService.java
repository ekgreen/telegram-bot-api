package com.goodboy.telegram.bot.spring.api.processor;

import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.meta.Bot;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

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
}
