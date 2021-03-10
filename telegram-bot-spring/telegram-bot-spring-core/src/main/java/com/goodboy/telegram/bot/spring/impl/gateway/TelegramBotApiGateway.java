/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.spring.impl.gateway;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.events.BotRegisteredEvent;
import com.goodboy.telegram.bot.spring.api.events.BotsReadyEvent;
import com.goodboy.telegram.bot.spring.api.gateway.*;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayBatchResponse.Fail;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.meta.Webhook;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@Infrastructure
public class TelegramBotApiGateway implements Gateway {

    // resolve bean name by bot names
    private final Map<String, String> beansNameResolver = new HashMap<>();
    // all routes registered in gateway by bot name
    private final Map<String, ApiGatewayRouting> routers = new HashMap<>();
    // all bots registered in gateway by bean name
    private final Map<String, Object> bots = new HashMap<>();
    // filters for additional around logic
    private final List<GatewayFilter> filters;
    // release fire and forget pattern - detach producer request from bot
    private final ExecutorService fireAndForget;
    // routing batch size
    private final int batchSize;

    public TelegramBotApiGateway(List<GatewayFilter> filters, @Nullable ExecutorService fireAndForget, @Nullable Integer batchSize) {
        this.filters = filters;
        this.fireAndForget = fireAndForget != null ? fireAndForget :
                Executors.newCachedThreadPool()
        ;
        this.batchSize = batchSize != null ? batchSize : 20;
    }

    private GatewayRouter findBotAndRouter(@NotNull String botName) {
        // 1. Базовая валидация запроса
        validateGatewayRequest(botName);

        // 2. Поиск маршрутизатора по имени
        final @Nonnull Object bot = findBotByName(botName);

        // 3. Поиск маршрутизатора по имени
        final @Nonnull ApiGatewayRouting router = resolveApiGatewayRoutingByName(botName);

        // 4. Попробуем смаршрутизировать запрос
        return new GatewayRouter(router, bot);
    }

    public void routing(@NotNull String botName, @NotNull Update update) {
        // get router - rout single update request
        final GatewayRouter router = findBotAndRouter(botName);

        // fire and forget
        router.routing(update);
    }

    public @NotNull GatewayBatchResponse routing(@NotNull String botName, @NotNull List<Update> updates) {
        try {
            // get router - rout single update request
            final GatewayRouter router = findBotAndRouter(botName); // throws exception

            // fire and forget
            return router.routing(updates); // not throws exception
        }catch (Exception exception){
            log.warn(String.format("[gateway] bot %s batch routing handled exception", botName), exception);
            final long failedCode = getFailedCode(exception);
            return new GatewayBatchResponse(GatewayBatchResponse.Status.FAILED)
                    .setFailed(updates.stream().map(update -> new Fail(failedCode, update)).collect(Collectors.toList()));
        }
    }

    private void validateGatewayRequest(@NotNull String botName) {
        if (!beansNameResolver.containsKey(botName))
            throw new TelegramApiGatewayException(404, "bot name not resolved = " + botName);
    }

    public @Nonnull
    Object findBotByName(@NotNull String botName) throws TelegramApiGatewayException {
        final @Nonnull String beanName = beansNameResolver.get(botName);

        if (!bots.containsKey(beanName))
            throw new TelegramApiGatewayException(404, "bean name not resolved = " + beanName);

        return bots.get(beanName);
    }

    public @Nonnull
    ApiGatewayRouting resolveApiGatewayRoutingByName(@NotNull String botName) throws TelegramApiGatewayException {
        if (!routers.containsKey(botName))
            throw new TelegramApiGatewayException(404, "bot name not resolved = " + botName);

        return routers.get(botName);
    }

    @EventListener(BotsReadyEvent.class)
    public void onRegistry(@NotNull BotsReadyEvent event) {
        final List<BotData> bots = event.getRegistryService().getAll();

        for (BotData data : bots) {
            // registry routes
            gatewayRouteRegistry(data);
            // registry name resolver
            gatewayNameInResolver(data);
        }

        final ApplicationContext context = event.getApplicationContext();

        beansNameResolver.values().forEach(beanName -> {
            this.bots.put(beanName, context.getBean(beanName));
        });
    }

    private void gatewayRouteRegistry(@NotNull BotData data) {
        final Class<?> originBotType = data.getOriginBotType();

        final Method[] declaredMethods = originBotType.getDeclaredMethods();

        // надо понять какие аннотации мы можем обрабатывать и в чем разница между ними, поидее для сервлета должна быть ни в чем
        // надо просто определить критерии для поиска вызываемых методов
        final List<WebhookGatewayRoutingDefinition> definitions = new ArrayList<>();

        for (final Method candidate : declaredMethods) {
            // все что касается API Телеграм и входящих запросов проходит через хуки
            final Webhook webhook = candidate.getDeclaredAnnotation(Webhook.class);

            if (webhook != null) {
                // провалидируем то что команды по которым пользователь хочет разделять хуки, соответсвуют текущему API
                validateCommands(webhook.command(), data.getCommands());

                // добавим определение хука в список
                definitions.add(createWebhookGatewayRoutingDefinition(candidate, webhook));
            }
        }

        // создатим роутер для данного бота и сохраним
        final ApiGatewayRouting botRouter = new ApiGatewayRouting(
                data.getName(),
                data.getGatewayRoutingResolver() != null ? data.getGatewayRoutingResolver() : new UniformWeightGatewayRoutingResolver(),
                definitions.stream().collect(toMap(
                        WebhookGatewayRoutingDefinition::getUid,
                        d -> d
                )),
                filters,
                fireAndForget,
                batchSize
        );

        // регистрируем наш роутер
        routers.put(data.getName(), botRouter);
    }

    private @Nonnull
    WebhookGatewayRoutingDefinition createWebhookGatewayRoutingDefinition(@Nonnull Method hook, @Nonnull Webhook webhook) {
        return new WebhookGatewayRoutingDefinition(hook, new GatewayRoutingDefinition()
                .setCommands(Set.of(webhook.command()))
        );
    }

    private void validateCommands(String[] declared, List<BotCommand> actualFromApi) {
        final Set<String> unique = actualFromApi.stream().map(BotCommand::getCommand).collect(Collectors.toSet());

        if (!unique.containsAll(List.of(declared)))
            throw new BeanCreationException("declared commands did not matched with actual api");
    }


    private void gatewayNameInResolver(@NotNull BotData data) {
        beansNameResolver.put(data.getName(), data.getBeanName());
    }


    @Slf4j
    @RequiredArgsConstructor
    private static class ApiGatewayRouting {

        // bot name - for clearly identify connectivity btw router and bot
        private final String botName;

        // gateway routing calculator
        private final GatewayRoutingResolver gatewayRoutingResolver;

        // defines routing constants which should be used for routing calculation
        private final Map<String, WebhookGatewayRoutingDefinition> webhookGatewayRoutingDefinitions;

        // filters provides around call logic
        private final List<GatewayFilter> filters;

        // fire and forget service
        private final ExecutorService fireAndForget;

        // fire batch size
        private final int batchSize;

        public Object routing(@Nonnull Object bot, @Nonnull Update update) throws TelegramApiGatewayException {
            // find rout
            final @Nonnull WebhookGatewayRoutingDefinition definition = resolveMostRelevantRout(update);

            // execute rout // todo batch logic?
            return fire(() -> execute(definition.getHook(), bot, update));
        }

        public GatewayBatchResponse routing(@Nonnull Object bot, @Nonnull List<Update> updates) throws TelegramApiGatewayException {
            // batch executed at all or
            final Multimap<String, Update> batches = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);
            // failed routes
            final List<Fail> failed = new ArrayList<>();

            for (final Update update : updates) {
                try {
                    // find rout
                    final @Nonnull WebhookGatewayRoutingDefinition hookDefinition = resolveMostRelevantRout(update);

                    batches.put(hookDefinition.uid, update);
                } catch (Exception exception) {
                    final long updateId = update.getUpdateId();
                    log.warn("router not found for update request { update_id = " + updateId + " }", exception);
                    failed.add(new Fail(getFailedCode(exception), update));
                }
            }

            // execute rout if failed batch size not equal initial
            if (failed.size() != updates.size()) {
                batches.keySet().forEach(definitionKey -> {
                    final WebhookGatewayRoutingDefinition definition = webhookGatewayRoutingDefinitions.get(definitionKey);

                    // get batch
                    final List<Update> batch = (List<Update>) batches.get(definitionKey);

                    // create partitions
                    final int batchSize = batch.size();

                    for (int i = 0; i < batchSize; ) {
                        final int lowBound = i;
                        final int upperBound = Math.min(i = i + this.batchSize, batchSize);
                        if (lowBound < upperBound) {
                            try {
                                fire(() -> {
                                    if (log.isTraceEnabled())
                                        log.trace(
                                                "[gateway] fire batch [from = {}, to = {}] to bot {}",
                                                batch.get(lowBound).getUpdateId(),
                                                batch.get(upperBound - 1).getUpdateId(),
                                                botName
                                        );

                                    for (int p = lowBound; p < upperBound; p++) {
                                        execute(definition.getHook(), bot, batch.get(p));
                                    }
                                });
                            } catch (Exception exception) {
                                log.warn("batch request out of quote", exception);
                                final long failedCode = getFailedCode(exception);
                                failed.addAll(batch.subList(lowBound, batchSize).stream().map(update -> new Fail(failedCode, update)).collect(Collectors.toList()));
                                break;
                            }
                        }
                    }
                });
            }

            // check again - coz fire also throws exceptions [429]
            return new GatewayBatchResponse(failed.size() != updates.size() ? GatewayBatchResponse.Status.SUCCESS : GatewayBatchResponse.Status.INCOMPLETE_ROUTE)
                    .setFailed(failed);
        }

        private Object fire(@NotNull Runnable fire) {
            try {
                fireAndForget.submit(fire);
            } catch (RejectedExecutionException exception) {
                throw new TelegramApiGatewayException(429, "to many update-requests", exception);
            }
            return null;
        }

        private Object execute(@Nonnull Method hook, @Nonnull Object bot, @Nonnull Update update) {
            try {
                return new VirtualGatewayFilterChain(filters, new ApiGatewayRoutingExecutor()).invoke(bot, hook, new Object[hook.getParameterCount()], update);
            } catch (Exception exception) {
                throw new TelegramApiGatewayException(500, "internal server route execution exception", exception);
            }
        }

        private @NotNull WebhookGatewayRoutingDefinition resolveMostRelevantRout(@Nonnull Update update) throws TelegramApiGatewayException {
            final GatewayUpdate gatewayUpdate = new GatewayUpdate(update);

            // default route - now it is always null
            WebhookGatewayRoutingDefinition hookDefinition = null;
            // very little weight, but over than 0 - brushes 0 value routes (0 means - not accepted)
            float current_hook_weight = 0;

            final Collection<WebhookGatewayRoutingDefinition> definitions = webhookGatewayRoutingDefinitions.values();

            for (WebhookGatewayRoutingDefinition definition : definitions) {
                final float weight = gatewayRoutingResolver.weight(definition.getDefinition(), gatewayUpdate);

                if (weight > current_hook_weight) {
                    current_hook_weight = weight;
                    hookDefinition = definition;
                }
            }

            if (hookDefinition == null)
                throw new TelegramApiGatewayException(404, "route for update not found");

            return hookDefinition;
        }

        @RequiredArgsConstructor
        private static class ApiGatewayRoutingExecutor implements GatewayFilterChain {

            @SneakyThrows
            public Object invoke(Object bot, Method hook, Object[] args, Update update) {
                return hook.invoke(bot, args);
            }
        }

        @RequiredArgsConstructor
        private static class VirtualGatewayFilterChain implements GatewayFilterChain {

            private final List<GatewayFilter> filters;
            private final GatewayFilterChain original;

            private int currentPosition = 0;

            @Override
            public Object invoke(Object proxy, Method method, Object[] args, Update update) {
                if (this.currentPosition == filters.size()) {
                    return this.original.invoke(proxy, method, args, update);
                } else {
                    final var processor = filters.get(this.currentPosition);
                    this.currentPosition = this.currentPosition + 1;

                    return processor.invoke(proxy, method, args, update, this);
                }
            }
        }
    }

    private static long getFailedCode(@NotNull Exception exception) {
        return exception instanceof TelegramApiGatewayException ?
                ((TelegramApiGatewayException) exception).getGatewayCode() : 500;
    }

    /**
     * Defines routing constants which should be used for routing calculation
     */
    @Data
    private static class WebhookGatewayRoutingDefinition {

        // definition unique id
        private final String uid = UUID.randomUUID().toString();

        // hook which defines in definition below
        private final Method hook;

        // definition of hook routing parameters
        private final GatewayRoutingDefinition definition;
    }

    @RequiredArgsConstructor
    private static class GatewayRouter {

        private final ApiGatewayRouting router;
        private final Object bot;

        public void routing(@Nonnull Update update) {
            router.routing(bot, update);
        }

        public @Nonnull GatewayBatchResponse routing(@Nonnull List<Update> updates) {
            return router.routing(bot, updates);
        }
    }

}
