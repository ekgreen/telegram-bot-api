package com.goodboy.telegram.bot.http.api.client.context;

import com.goodboy.telegram.bot.api.BotCommand;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Данные о боте и его окружении вместе с клиентским запросом к сервису
 */
public interface UpdateContext {

    /**
     * @return идентификатор бота в телеграм
     */
    @Nonnull Integer getBotId();

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
