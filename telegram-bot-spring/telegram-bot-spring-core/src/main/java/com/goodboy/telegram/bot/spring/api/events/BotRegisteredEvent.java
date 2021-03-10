package com.goodboy.telegram.bot.spring.api.events;

import com.goodboy.telegram.bot.spring.api.data.BotData;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class BotRegisteredEvent extends ApplicationEvent {

    @Getter
    private final BotData botData;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public BotRegisteredEvent(Object source, BotData botData) {
        super(source);
        this.botData = botData;
    }
}
