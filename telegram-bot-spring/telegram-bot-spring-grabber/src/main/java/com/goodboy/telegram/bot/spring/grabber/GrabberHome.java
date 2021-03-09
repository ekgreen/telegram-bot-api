package com.goodboy.telegram.bot.spring.grabber;

import com.goodboy.telegram.bot.api.methods.updates.TelegramGetUpdatesApi;
import com.goodboy.telegram.bot.spring.api.gateway.Gateway;
import org.springframework.core.env.Environment;

public class GrabberHome extends GrabberSpace {

    public GrabberHome(Environment environment, Gateway gateway, TelegramGetUpdatesApi api) {
        super(environment, gateway, api);
    }

    public void run() {
        // save in state
        executors.forEach((name,grabber) -> {
            final Thread botExecutionThread = new Thread(() -> {
                while (!interrupted)
                    grabber.grabAndExecuteUpdates();
            });

            registryNewThread(botExecutionThread);
            botExecutionThread.start();
        });
    }
}
