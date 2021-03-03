package com.goodboy.telegram.bot.spring.api.processor.arguments;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.processor.BotData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
