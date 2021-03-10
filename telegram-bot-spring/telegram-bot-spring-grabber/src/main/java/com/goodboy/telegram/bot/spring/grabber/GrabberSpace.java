package com.goodboy.telegram.bot.spring.grabber;

import com.goodboy.telegram.bot.api.methods.updates.TelegramGetUpdatesApi;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.events.BotRegisteredEvent;
import com.goodboy.telegram.bot.spring.api.events.BotsReadyEvent;
import com.goodboy.telegram.bot.spring.api.gateway.Gateway;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class GrabberSpace implements ConcurrentGrabbersRunner {

    private final static String FIND_CONFIGURATION_TEMPLATE = "telegram.%s.grabber.%s";

    private final Environment environment;

    private final Gateway gateway;
    private final TelegramGetUpdatesApi api;

    protected final Map<String, TelegramBotGrabber> executors = new HashMap<>();

    protected volatile boolean interrupted = false;
    private List<Thread> executingThreads = new ArrayList<>();

    public abstract void run();

    protected @NotNull Thread registryNewThread(@NotNull Runnable task) {
        final Thread thread = new Thread(task);
        executingThreads.add(thread);
        return thread;
    }

    @Override
    public void destroy() throws Exception {
        if (executingThreads == null)
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "grabber not initialized or already shutdown!");

        interrupted = true;
        executingThreads.forEach(Thread::interrupt);
        executingThreads = null;
    }

    @EventListener(BotsReadyEvent.class)
    public void onBotsReady(@NotNull BotsReadyEvent event) {
        final List<BotData> list = event.getRegistryService().getAll();

        for (BotData data : list) {
            final String botName = data.getName();
            final TelegramApiTokenResolver tokenResolver = data.getTelegramApiTokenResolver();

            // registry grabber configuration
            registryGrabberConfigurationByBotName(botName, tokenResolver);
        }

        run(); // non blocking!
    }

    protected void updateGrabberConfiguration(GrabberConfiguration configuration) {
    }

    private void registryGrabberConfigurationByBotName(@NotNull String botName, @NotNull TelegramApiTokenResolver tokenResolver) {
        final GrabberConfiguration configuration = new GrabberConfiguration(botName, tokenResolver)
                .setLimit(getProperty(botName, "limit", Integer.class))
                .setTimeout(getProperty(botName, "timeout", Long.class))
                .setAllowedUpdates(this.getProperty(botName, "allowed_updates", List.class));

        updateGrabberConfiguration(configuration);

        executors.put(botName, new TelegramBotGrabber(botName, api, gateway, configuration, new SingleThreadGrabberUpdateCache()));
    }

    private <T> @Nullable T getProperty(@NotNull String botName, @NotNull String key, Class<?> propertyType) {
        return (T) environment.getProperty(String.format(FIND_CONFIGURATION_TEMPLATE, botName, key), propertyType);
    }
}
