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

package com.goodboy.telegram.bot.spring.impl.processors.annotations.webhook;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.methods.action.Action;
import com.goodboy.telegram.bot.http.api.client.update.ModifiableUpdateProvider;
import com.goodboy.telegram.bot.spring.api.actions.FastAction;
import com.goodboy.telegram.bot.spring.api.actions.FutureFastAction;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.meta.ChatId;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.meta.Webhook;
import com.goodboy.telegram.bot.spring.api.processor.*;
import com.goodboy.telegram.bot.spring.api.processor.arguments.TypeArgumentEvaluator;
import com.goodboy.telegram.bot.spring.impl.processors.origin.OriginFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Infrastructure
public class WebhookAnnotationProcessor implements BotAnnotationProcessor, ApplicationContextAware {

    private final static String DEFAULT_EXECUTOR_NAME = "defaultWebhookAnnotationProcessorExecutorService";

    private final BotAnnotationProcessorFactory originFactory;
    private final HeavyweightScheduler scheduler;
    private final ModifiableUpdateProvider updateProvider;
    private final TypeArgumentEvaluator typeArgumentEvaluator;

    private final Map<String, ExecutorService> heavyweightExecutorServices = new HashMap<>();

    private Set<String> executorBeanNames = new HashSet<>();
    private ApplicationContext context;

    @Autowired
    public WebhookAnnotationProcessor(@OriginFactory BotAnnotationProcessorFactory originFactory, HeavyweightScheduler scheduler, ModifiableUpdateProvider updateProvider, TypeArgumentEvaluator evaluator) {
        this.originFactory = originFactory;
        this.scheduler = scheduler;
        this.updateProvider = updateProvider;
        this.typeArgumentEvaluator = evaluator;
    }

    public @Nonnull
    BotMethodProcessorChain createAnnotationProcessor(@Nonnull BotData data, @Nonnull Method method) {
        if (!method.isAnnotationPresent(supports()))
            throw new IllegalArgumentException(format("attempt to create webhook annotation processor with non-annotated method [%s] by %s",
                    method.getName(), supports().getSimpleName()
            ));

        return createWebhookAnnotationProcessor(data, method, originFactory.createAnnotationProcessor(data, method));
    }

    public Class<? extends Annotation> supports() {
        return Webhook.class;
    }

    private BotMethodProcessorChain createWebhookAnnotationProcessor(@Nonnull BotData botData, @Nonnull Method method, @Nonnull BotMethodProcessorChain origin) {
        // find declared annotation
        final Webhook webhook = method.getDeclaredAnnotation(Webhook.class);
        // check webhook type - if LIGHTWEIGHT return origin
        // SNEAKY is not released yet seem so executing like LIGHTWEIGHT (very sneaky)
        final Webhook.ExecutionType type = webhook.type();

        if (type == Webhook.ExecutionType.LIGHTWEIGHT || type == Webhook.ExecutionType.SNEAKY)
            return origin;

        // HEAVYWEIGHT - checkout executing thread if executing thread missed - create and provide default
        final String registeredHeavyweightExecutorServiceBeanName = registryHeavyweightExecutorService(webhook.heavyWeightExecutorService());

        // FAST ACTIONS - check return type and its generic, read data from annotation
        // return type got high priority
        final boolean isFastActionPoweredByWebhookAnnotation;
        final boolean isFastActionPoweredByReturnType;

        final Action[] actions = webhook.action();
        final Class<?> returnType = method.getReturnType();

        // checkout fast action signs
        if ((isFastActionPoweredByWebhookAnnotation = actions.length != 0) | (isFastActionPoweredByReturnType = FastAction.class.isAssignableFrom(returnType))) {
            // heavyweight with fast action
            return new HeavyweightBotMethodProcessorChain(
                    method,
                    botData,
                    registeredHeavyweightExecutorServiceBeanName,
                    origin,
                    isFastActionPoweredByReturnType,
                    isFastActionPoweredByWebhookAnnotation ?
                            new WebhookFastActionProvider(actions)
                            :
                            null
            );
        } else {
            // heavyweight without fast action
            return new HeavyweightBotMethodProcessorChain(method, botData, registeredHeavyweightExecutorServiceBeanName, origin);
        }
    }

    private @Nonnull
    String registryHeavyweightExecutorService(@Nonnull String heavyWeightExecutorService) {
        // heavyweight executor service bean name
        final String heavyWeightExecutorServiceBeanName;

        if (StringUtils.isNotEmpty(heavyWeightExecutorService)) {
            heavyWeightExecutorServiceBeanName = heavyWeightExecutorService;

            if (context == null)
                throw new BeanInitializationException("missed application context");

            final AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();

            if (factory instanceof ConfigurableListableBeanFactory) {
                final var configurableListableBeanFactory = (ConfigurableListableBeanFactory) factory;

                final BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(heavyWeightExecutorService);

                Class<?> executorType;
                if ((executorType = ReflectionUtils.forName(beanDefinition.getBeanClassName())) == null || !ExecutorService.class.isAssignableFrom(executorType))
                    throw new BeanInitializationException("declared executor bean name [" + heavyWeightExecutorService + "] not found in context or type [" + executorType + "] not assignable to ExecutorService");

                executorBeanNames.add(heavyWeightExecutorService);
            } else {
                final Set<String> beanDefinitionNames = Set.of(context.getBeanDefinitionNames());

                // assignability will checked in runtime cast
                if (!beanDefinitionNames.contains(heavyWeightExecutorService))
                    throw new BeanInitializationException("declared executor bean name [" + heavyWeightExecutorService + "] not found in context");

                executorBeanNames.add(heavyWeightExecutorService);
            }
        } else {
            if (!heavyweightExecutorServices.containsKey(heavyWeightExecutorServiceBeanName = DEFAULT_EXECUTOR_NAME))
                heavyweightExecutorServices.put(DEFAULT_EXECUTOR_NAME, createDefaultExecutorService());
        }

        return heavyWeightExecutorServiceBeanName;
    }

    private ExecutorService createDefaultExecutorService() {
        return Executors.newFixedThreadPool(4);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void findExecutors() {
        executorBeanNames.forEach(beanName -> {
            heavyweightExecutorServices.put(beanName, (ExecutorService) context.getBean(beanName));
        });
        // clean gc
        executorBeanNames = null;
        // clean after initialisation redundant context
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private static class WebhookFastActionProvider implements Supplier<Action> {
        private final Action[] actions;
        private int position;

        private WebhookFastActionProvider(Action[] actions) {
            this.actions = actions;
            this.position = Math.max(actions.length - 1, 0);
        }

        public @NotNull Action get() {
            return actions[position = (position + 1) % actions.length]; // cycling action flow
        }
    }

    private class HeavyweightBotMethodProcessorChain implements BotMethodProcessorChain {

        // origin calling method
        private final Method originWebhookMethod;

        // data about bot
        private final BotData botData;

        // executor service bean name - calculated on context creation phase
        private final String executorServiceBeanName;

        // is power method powered by (annotated or return type) fast action
        private final boolean isOriginMethodPoweredByFastAction;

        // origin chain executor
        private final BotMethodProcessorChain origin;

        // if origin method powered by return type  fast action = true
        private final boolean isOriginMethodReturnFastAction;

        // if origin method powered by annotation = present
        private final Supplier<Action> actionProvider;

        // resolve chat id for notification request
        private final Function<Object[], String> chaIdResolver;

        // heavyweight method without fast action
        public HeavyweightBotMethodProcessorChain(
                @Nonnull Method originWebhookMethod,
                @Nonnull  BotData botData,
                @Nonnull String executorServiceBeanName,
                @Nonnull BotMethodProcessorChain origin
        ) {
            this(originWebhookMethod, botData, executorServiceBeanName, false, origin, false, null);
        }

        // heavyweight method with fast action
        public HeavyweightBotMethodProcessorChain(
                @Nonnull Method originWebhookMethod,
                @Nonnull  BotData botData,
                @Nonnull String executorServiceBeanName,
                @Nonnull BotMethodProcessorChain origin,
                boolean isOriginMethodReturnFastAction,
                @Nullable Supplier<Action> actionProvider
        ) {
            this(originWebhookMethod, botData, executorServiceBeanName, isOriginMethodReturnFastAction || actionProvider != null, origin, isOriginMethodReturnFastAction, actionProvider);
        }

        public HeavyweightBotMethodProcessorChain(Method originWebhookMethod, BotData botData, String executorServiceBeanName, boolean isOriginMethodPoweredByFastAction, BotMethodProcessorChain origin, boolean isOriginMethodReturnFastAction, Supplier<Action> actionProvider) {
            this.originWebhookMethod = originWebhookMethod;
            this.botData = botData;
            this.executorServiceBeanName = executorServiceBeanName;
            this.isOriginMethodPoweredByFastAction = isOriginMethodPoweredByFastAction;
            this.origin = origin;
            this.isOriginMethodReturnFastAction = isOriginMethodReturnFastAction;
            this.actionProvider = actionProvider;
            this.chaIdResolver = createChatIdResolver();
        }

        private Function<Object[], String> createChatIdResolver() {
            final Class<?>[] parameterTypes = originWebhookMethod.getParameterTypes();

            // 1) try to find parameter with ChatId - easiest way to get value - it will be early evaluated for as
            int temporalChatIdPosition = -1;

            for (int p = 0; p < parameterTypes.length; p++) {
                if(parameterTypes[p].isAnnotationPresent(ChatId.class)){
                    temporalChatIdPosition = p;
                    break;
                }
            }

            // 2) try to find parameter with Update - second by complexity way to get value - it will be early evaluated for as
            int temporalUpdateIdPosition = -1;

            for (int p = 0; p < parameterTypes.length; p++) {
                if(Update.class.isAssignableFrom(parameterTypes[p])){
                    temporalUpdateIdPosition = p;
                    break;
                }
            }

            // rewrite values in final
            final int chatIdPosition = temporalChatIdPosition;
            final int updateIdPosition = temporalUpdateIdPosition;

            return (args) -> {
                // multitype variable
                Object argument;

                if (chatIdPosition > -1 && (argument = args[chatIdPosition]) != null)
                    return (String) argument;

                if ((updateIdPosition > -1 && (argument = args[updateIdPosition]) != null) || (argument = updateProvider.getUpdate()) != null)
                    return typeArgumentEvaluator.getChatId((Update) argument);

                return null;
            };
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            // check if origin method powered by fast action do logic
            if (isOriginMethodPoweredByFastAction) {
                // method that should be detached
                Runnable heavyweight;
                //local action provider
                Supplier<Action> local;
                // check property means that origin method return type is FastAction<? extends APi>
                if (isOriginMethodReturnFastAction) {
                    // origin chain must return fast action
                    final FastAction<? extends Api> invoke = (FastAction<? extends Api>) origin.invoke(proxy, method, args);
                    // local action provider will be connected with returned fast action
                    local = invoke::fastAction;
                    // fast action heavyweight method must be detached not origin
                    heavyweight = invoke::heavyweight;
                } else {
                    // action provider present must be present - it is webhook provider
                    local = actionProvider;
                    // detach origin method
                    heavyweight = () -> origin.invoke(proxy, method, args);
                }
                // already detached method
                final Future<?> detached = detach(heavyweight);
                // the idea is polling executing thread each N seconds and if it is not ready
                // send fast action
                // registry fast action future in scheduler
                scheduler.poll(new FutureFastAction(chaIdResolver.apply(args), botData.getTelegramApiTokenResolver(), detached, local));
            } else {
                // just detach
                detach(() -> origin.invoke(proxy, method, args));
            }
            // as a part of bot api contract, there is no response body for calling client
            return null;
        }

        private Future<?> detach(Runnable botMethod) {
            // transfer update btw providers
            final Update currentThreadUpdate = updateProvider.getUpdate();
            // method -> detached
            return heavyweightExecutorServices.get(executorServiceBeanName).submit(new ContextualHeavyweightThreadExecutor(botMethod));
        }
    }

    private class ContextualHeavyweightThreadExecutor implements Runnable{

        private final Runnable origin;
        private final Update parentThreadUpdate;

        public ContextualHeavyweightThreadExecutor(Runnable origin) {
            this.origin = origin;
            this.parentThreadUpdate = updateProvider.getUpdate();
        }

        @Override
        public void run() {
            try {
                // add parent thread update to current
                updateProvider.setUpdate(parentThreadUpdate);
                // execute origin
                origin.run();
            } finally {
                // clean provider from request
                updateProvider.clean();
            }
        }
    }

}
