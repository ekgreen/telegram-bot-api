package com.goodboy.telegram.bot.spring.impl.gateway;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.events.OnBotRegistry;
import com.goodboy.telegram.bot.spring.api.gateway.*;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.meta.Webhook;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Infrastructure
@RequiredArgsConstructor
public class TelegramBotApiGateway implements Gateway, OnBotRegistry {

    // resolve bean name by bot names
    private final Map<String, String> beansNameResolver = new HashMap<>();
    // all routes registered in gateway by bot name
    private final Map<String, ApiGatewayRouting> routers = new HashMap<>();
    // all bots registered in gateway by bean name
    private final Map<String, Object> bots = new HashMap<>();
    // filters for additional around logic
    private final List<GatewayFilter> filters;

    @EventListener(ContextRefreshedEvent.class)
    public void grabBotsInGateway(ContextRefreshedEvent event) {
        final ApplicationContext context = event.getApplicationContext();

        beansNameResolver.values().forEach(beanName -> {
            bots.put(beanName, context.getBean(beanName));
        });
    }

    public Object routing(@NotNull String botName, @NotNull Update update) {
        // 1. Базовая валидация запроса
        validateGatewayRequest(botName, update);

        // 2. Поиск маршрутизатора по имени
        final @Nonnull Object bot = findBotByName(botName);

        // 3. Поиск маршрутизатора по имени
        final @Nonnull ApiGatewayRouting router = resolveApiGatewayRoutingByName(botName);

        // 4. Попробуем смаршрутизировать запрос
        return router.routing(bot, update);
    }

    private void validateGatewayRequest(@NotNull String botName, @NotNull Update update) {
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

    public void onRegistry(@NotNull BotData data) {
        // registry routes
        gatewayRouteRegistry(data);
        // registry name resolver
        gatewayNameInResolver(data);
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
                data.getGatewayRoutingResolver() != null ? data.getGatewayRoutingResolver() : new UniformWeightGatewayRoutingResolver(),
                definitions,
                filters
        );

        // регистрируем наш роутер
        routers.put(data.getName(), botRouter);
    }

    private @Nonnull WebhookGatewayRoutingDefinition createWebhookGatewayRoutingDefinition(@Nonnull Method hook, @Nonnull  Webhook webhook) {
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


    @RequiredArgsConstructor
    private static class ApiGatewayRouting {

        // gateway routing calculator
        private final GatewayRoutingResolver gatewayRoutingResolver;

        // defines routing constants which should be used for routing calculation
        private final List<WebhookGatewayRoutingDefinition> webhookGatewayRoutingDefinitions;

        // filters provides around call logic
        private final List<GatewayFilter> filters;

        public Object routing(@Nonnull Object bot, @Nonnull Update update) throws TelegramApiGatewayException {
            // find rout
            final @Nonnull Method hook = resolveMostRelevantRout(update);

            // execute rout
            return execute(hook, bot, update);
        }

        private Object execute(@Nonnull Method hook, @Nonnull Object bot, @Nonnull Update update) {
            try {
                return new VirtualGatewayFilterChain(filters, new ApiGatewayRoutingExecutor()).invoke(bot, hook, new Object[hook.getParameterCount()], update);
            } catch (Exception exception) {
                throw new TelegramApiGatewayException(500, "internal server route execution exception", exception);
            }
        }

        private @Nonnull Method resolveMostRelevantRout(@Nonnull Update update) throws TelegramApiGatewayException{
            final GatewayUpdate gatewayUpdate = new GatewayUpdate(update);

            // default route - now it is always null
            Method hook = null;
            // very little weight, but over than 0 - brushes 0 value routes (0 means - not accepted)
            float current_hook_weight = 0;

            for (WebhookGatewayRoutingDefinition definition : webhookGatewayRoutingDefinitions) {
                final float weight = gatewayRoutingResolver.weight(definition.getDefinition(), gatewayUpdate);

                if(weight > current_hook_weight) {
                    current_hook_weight = weight;
                    hook = definition.getHook();
                }
            }

            if(hook == null)
                throw new TelegramApiGatewayException(404, "route for update not found");

            return hook;
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

    /**
     * Defines routing constants which should be used for routing calculation
     */
    @Data
    private static class WebhookGatewayRoutingDefinition{

        // hook which defines in definition below
        private final Method hook;

        // definition of hook routing parameters
        private final GatewayRoutingDefinition definition;
    }

}
