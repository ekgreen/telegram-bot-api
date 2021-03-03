package com.goodboy.telegram.bot.spring.impl.processors.arguments;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiUpdateContextImpl;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.processor.BotData;
import com.goodboy.telegram.bot.spring.api.processor.arguments.TypeArgumentProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

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
