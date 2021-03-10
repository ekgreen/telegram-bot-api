package com.goodboy.telegram.bot.spring.grabber;

import com.goodboy.telegram.bot.api.methods.updates.TelegramGetUpdatesApi;
import com.goodboy.telegram.bot.spring.api.gateway.Gateway;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

public class GrabberDorm extends GrabberSpace {

    public GrabberDorm(Environment environment, Gateway gateway, TelegramGetUpdatesApi api) {
        super(environment, gateway, api);
    }

    @Override
    public void run() {
        if (executors.size() > 0) {
            registryNewThread(() -> {
                while (!interrupted) {
                    executors.forEach((name, executor) -> {
                        executor.grabAndExecuteUpdates();
                    });
                }
            }).start();
        }
    }

    @Override
    protected void updateGrabberConfiguration(GrabberConfiguration configuration) {
        configuration.setTimeout(0L);
    }
}
