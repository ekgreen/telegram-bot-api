package com.goodboy.telegram.bot.spring.impl.listeners;

import com.goodboy.telegram.bot.api.method.webhook.SetWebhookApi;
import com.goodboy.telegram.bot.api.method.webhook.TelegramWebhookApi;
import com.goodboy.telegram.bot.api.method.webhook.WebhookInfo;
import com.goodboy.telegram.bot.api.request.DelayedByteArrayUploading;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.spring.api.bot.Bot;
import com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition;
import com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition.RegistryStatus;
import com.goodboy.telegram.bot.spring.api.listeners.OnBotCreationListener;
import com.goodboy.telegram.bot.spring.api.providers.UrlResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import static com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition.RegistryBot.NON;
import static com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition.RegistryBot.REWRITE;

@Slf4j
@RequiredArgsConstructor
public class WebhookBotRegistry implements OnBotCreationListener {

    private final UrlResolver urlResolver;
    private final TelegramWebhookApi webhookApi;

    private volatile Map<String, RegistryStatus> tickets = new ConcurrentHashMap<>();

    // clean me after initialisation
    private ExecutorService executors = new ForkJoinPool();
    private AtomicInteger registryCounter = new AtomicInteger();

    @Override
    public void onBotCreation(@NotNull Bot webhook, @NotNull BotBeanDefinition definition) {
        if (definition.getSelfRegistry() != NON) {
            registry(definition);
        }
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void refresh(ContextRefreshedEvent event) {
        CompletableFuture
                .runAsync(() -> {
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
                })
                .thenRunAsync(() -> {
                    executors.shutdown();

                    // clean memory
                    executors = null;
                    registryCounter = null;
                });
    }


    private void registry(@NotNull BotBeanDefinition definition) {
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
            @NotNull TelegramCoreResponse<WebhookInfo> response = webhookApi.getWebhookInfo(request -> request.setToken(token));

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
                                    byte[] certificate = definition.getCertificateProvider().certificate(definition.getBotName());

                                    api = new SetWebhookApi()
                                            .setUrl(urlResolver.getContextUrl(definition.getPath()))
                                            .setCertificate(certificate != null ? new DelayedByteArrayUploading("pk_spring_boot.pem", () -> certificate) : null)
                                            .setMaxConnections(definition.getMaxConnections());

                                    var optional = webhookApi.setWebhook(request -> request
                                            .setToken(token)
                                            .setBody(api)
                                    ).get();

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
