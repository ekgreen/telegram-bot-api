package com.goodboy.telegram.bot.http.api.client.context;

/**
 * Интерфейс для получения контекста разными способами, например контекст может быть thread-local или может передаваться
 * вместе с аргументами в параметрах для реактивного тсека
 */
public interface TelegramApiContextResolver {

    /**
     * Метод получения контекста из источника
     *
     * @return контекст
     */
    UpdateContext read();
}