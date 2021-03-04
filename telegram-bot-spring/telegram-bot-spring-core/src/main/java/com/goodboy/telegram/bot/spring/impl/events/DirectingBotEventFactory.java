package com.goodboy.telegram.bot.spring.impl.events;

import com.goodboy.telegram.bot.spring.api.events.BotEventFactory;
import com.goodboy.telegram.bot.spring.api.events.OnBotRegistry;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Infrastructure
public class DirectingBotEventFactory implements BotEventFactory {

    // beans waiting on registry event
    private List<OnBotRegistry> waitingRegistryEvent;

    @Override
    public void createOnRegistryEvent(@NotNull BotData botData) {
        if(waitingRegistryEvent != null && !waitingRegistryEvent.isEmpty())
            waitingRegistryEvent.forEach(consumer -> consumer.onRegistry(botData));
    }

    @Autowired
    public void setWaitingRegistryEvent(@Nullable List<OnBotRegistry> waitingRegistryEvent) {
        this.waitingRegistryEvent = waitingRegistryEvent;
    }
}
