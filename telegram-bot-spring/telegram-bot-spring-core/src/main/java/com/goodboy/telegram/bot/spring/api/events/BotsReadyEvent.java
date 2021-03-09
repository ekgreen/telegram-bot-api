package com.goodboy.telegram.bot.spring.api.events;

import com.goodboy.telegram.bot.spring.api.processor.BotRegistryService;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

public class BotsReadyEvent extends ApplicationEvent {

    @Getter
    private final BotRegistryService registryService;

    @Getter
    private final ApplicationContext context;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param registryService bot registry service
     */
    public BotsReadyEvent(Object source, BotRegistryService registryService, ApplicationContext context) {
        super(source);
        this.registryService = registryService;
        this.context = context;
    }
}
