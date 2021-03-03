package com.goodboy.telegram.bot.http.api.client.context;

import javax.annotation.Nonnull;

/**
 * Расширение базового интерфейса по получению контекста из определенного ресурса. Расширение заключается в возможности
 * управлять жизненым циклом контекста, а именно устанавливать и удалять его
 */
public interface TelegramApiContextHandler extends TelegramApiContextResolver{

    /**
     * Создание (задание) контекста для определнного ресурса
     *
     * @param context контекст
     */
    void create(@Nonnull UpdateContext context);

    /**
     * Удаление контекста из ресурса
     */
    void delete();
}
