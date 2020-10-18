package com.goodboy.telegram.bot.spring.processors;

import com.goodboy.telegram.bot.api.method.webhook.SetWebhookApi;
import com.goodboy.telegram.bot.api.method.webhook.UnauthorizedTelegramWebhookApi;
import com.goodboy.telegram.bot.api.method.webhook.WebhookInfo;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.spring.meta.Webhook;
import com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition;
import com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition.RegistryStatus;
import com.goodboy.telegram.bot.spring.meta.BotWebhookListener;
import com.goodboy.telegram.bot.spring.providers.UrlResolver;
import com.goodboy.telegram.bot.spring.toolbox.InfrastructureBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import static com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition.RegistryBot.NON;
import static com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition.RegistryBot.REWRITE;

@Slf4j
@RequiredArgsConstructor
public class WebhookBotRegistry implements BotWebhookListener {

    private final UrlResolver urlResolver;
    private final UnauthorizedTelegramWebhookApi webhookApi;

    private volatile Map<String, RegistryStatus> tickets = new ConcurrentHashMap<>();

    // clean me after initialisation
    private ExecutorService executors = new ForkJoinPool();
    private AtomicInteger registryCounter = new AtomicInteger();

    @Override
    public void onWebhookCreation(@NotNull Webhook webhook, @NotNull WebhookBeanDefinition definition) {
        if (definition.getSelfRegistry() != NON) {
            registry(definition);
        }
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void refresh(ContextRefreshedEvent event) {
        CompletableFuture
                .supplyAsync(() -> {
                    while (true) {
                        if (registryCounter.get() == 0) {
                            final HashMap<String, RegistryStatus> changeMap = new HashMap<>(tickets);

                            // atomic
                            this.tickets = changeMap;
                            break;
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return true;
                })
                .completeAsync(() -> {
                    executors.shutdown();

                    // clean memory
                    executors = null;
                    registryCounter = null;

                    return true;
                });
    }


    private void registry(@NotNull WebhookBeanDefinition definition) {
        final String ticket = definition.getBotName();

        // 1) зарегестрируем тикет на регистрацию бота
        tickets.put(ticket, RegistryStatus.IN_PROCESS);
        registryCounter.incrementAndGet();

        // 2) проставим в описание webhook метод для определения регистрации сервиса
        definition.setRegistryStatusPules(() -> tickets.get(ticket));

        // 2) запулим исполняться тикет на регистрацию бота
        executors.execute(() -> {
            // 1) сгенерируем токен из описания webhook
            final String token = definition.getTokenHandler().token(definition.getBotName());

            // 2) определяем зарегестрировал ли webhook
            @Nonnull TelegramCoreResponse<WebhookInfo> response = webhookApi.getWebhookInfo(token);

            final Runnable registry = () -> {
                tickets.put(ticket, RegistryStatus.REGISTERED);
                registryCounter.decrementAndGet();
            };

            final Runnable error = () -> {
                tickets.put(ticket, RegistryStatus.ERROR);
                registryCounter.decrementAndGet();
            };

            response
                    .get()
                    .ifPresentOrElse(
                            webhookInfo -> {
                                SetWebhookApi api;

                                @Nullable String url = webhookInfo.getUrl();

                                if ((StringUtils.isNotEmpty(url) && definition.getSelfRegistry() == REWRITE) || (StringUtils.isEmpty(url))) {
                                    api = new SetWebhookApi()
                                            .setUrl(urlResolver.getContextUrl(definition.getPath()))
                                            .setCertificate(definition.getCertificateProvider().certificate(definition.getBotName()))
                                            .setMaxConnections(definition.getMaxConnections());

                                    var optional = webhookApi.setWebhook(token, api).get();

                                    if (optional.isPresent() && optional.get())
                                        registry.run();
                                    else
                                        error.run();
                                }else
                                    registry.run();
                            },
                            error
                    );
        });
    }

}
