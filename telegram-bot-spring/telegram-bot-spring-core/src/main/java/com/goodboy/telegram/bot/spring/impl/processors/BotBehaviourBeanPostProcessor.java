package com.goodboy.telegram.bot.spring.impl.processors;

import com.goodboy.telegram.bot.http.api.client.context.TelegramApiContextHandler;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.meta.Bot;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.processor.*;
import com.goodboy.telegram.bot.spring.api.processor.arguments.BotArgumentProcessor;
import com.goodboy.telegram.bot.spring.api.processor.arguments.BotArgumentProcessorFactory;
import com.goodboy.telegram.bot.spring.impl.processors.arguments.TransformDataToContextArgumentProcessor;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.lang.Nullable;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Infrastructure
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BotBehaviourBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, BotMetadata> bots = new HashMap<>();

    private final TelegramApiContextHandler apiContextHandler;
    private final BotRegistryService botRegistryService;

    private final BotArgumentProcessorFactory botArgumentProcessorFactory;
    private final BotAnnotationProcessorFactory botAnnotationProcessorFactory;
    private List<BotMethodProcessor> botMethodProcessors = ImmutableList.of();

    @Getter
    @RequiredArgsConstructor
    private static class BotMetadata {
        private final Class<?> nativeBotType;
        private final Bot annotation;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nonnull String beanName) throws BeansException {
        final Class<?> beanClass = bean.getClass();
        final Bot annotation;

        if ((annotation = beanClass.getAnnotation(Bot.class)) != null) {
            bots.put(beanName, new BotMetadata(beanClass, annotation));
        }

        return bean;
    }

    @SneakyThrows
    public Object postProcessAfterInitialization(final @Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        // 1. Проверяем, был ли бин записан как бот
        if (bots.containsKey(beanName)) {
            // 2. Должны получить метаинформацию о боте
            final BotMetadata bot = bots.get(beanName);

            // 2.1 Получим сразу все даннные, чтобы 50 раз не обращаться к метаданным
            final Class<?> controllerType = bot.getNativeBotType();
            final Bot annotation = bot.getAnnotation();

            // 3. Регистрируем данные о боте
            final BotData botData = botRegistryService.registryBot(beanName, controllerType);

            // 4. Инициализируем описание для каждого желанного метода
            final Map<Method, BotMethodProcessorChain> processors = createCallMethodDefinitions(controllerType, annotation, botData);

            // 5. Создаем proxy над контроллером, исопльзуя cglib
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(controllerType);
            enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) -> {
                BotMethodProcessorChain holder;

                // handled by our enhancer
                if ((holder = processors.get(method)) != null)
                    return holder.invoke(bean, method, args);

                // skip
                return method.invoke(bean, method, args);
            });

            return enhancer.create();
        }

        return bean;
    }

    private Map<Method, BotMethodProcessorChain> createCallMethodDefinitions(@Nonnull Class<?> controllerType, @Nonnull Bot annotation, @Nonnull BotData botData) {
        final Map<Method, BotMethodProcessorChain> holders = new HashMap<>();

        final Method[] methods = controllerType.getDeclaredMethods();
        for (Method method : methods) {
            if (botAnnotationProcessorFactory.supports(method)) {

                // 1. Процессор предоставляет оригинальный механизм для исполнения текущего метода
                final BotMethodProcessorChain origin = botAnnotationProcessorFactory.createAnnotationProcessor(method);

                // 2. Создадим копию доступных процессоров, добавим в них технические, создаваемые
                final var copy = new ArrayList<>(this.botMethodProcessors);

                var proxyType = annotation.proxyType();

                // все что ниже более широкий тип чем предыдущий ( без брейков )
                switch (proxyType) {
                    case THREAD_SCOPE:
                        copy.add(new BotThreadScopedContextMethodHolder(botData));
                    case ARGUMENT_SCOPE:
                        copy.add(new BotArgumentScopedContextMethodHolder(botArgumentProcessorFactory.createArgumentProcessor(method), botData));
                }

                // 3. Профильтруем процессоры на соответсвие фильтру и отсортируем им от меньшего к большему
                final var orderedProcessorsForCurrentMethod = copy.stream()
                        .filter(processor -> processor.supports(method))
                        .sorted(new Comparator<>() {
                            public int compare(BotMethodProcessor o1, BotMethodProcessor o2) {
                                return Integer.compare(order(o1), order(o2));
                            }

                            private int order(BotMethodProcessor o) {
                                int order = o.getOrder();

                                if (o.getClass().isAnnotationPresent(PlatformProcessor.class))
                                    return order;

                                return Math.max(0, order);
                            }
                        })
                        .collect(Collectors.toList());


                holders.put(method, new BotExecutionMethodHolder(origin, orderedProcessorsForCurrentMethod));
            }
        }

        return holders;
    }

    @RequiredArgsConstructor
    private static class VirtualBotMethodProcessorChain implements BotMethodProcessorChain {

        private final BotMethodProcessorChain original;
        private final List<BotMethodProcessor> processors;

        private int currentPosition = 0;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            if (this.currentPosition == this.processors.size()) {
                return this.original.invoke(proxy, method, args);
            } else {
                final var processor = this.processors.get(this.currentPosition);
                this.currentPosition = this.currentPosition + 1;

                return processor.invoke(proxy, method, args, this);
            }
        }
    }

    @RequiredArgsConstructor
    private static class BotExecutionMethodHolder implements BotMethodProcessorChain {

        private final BotMethodProcessorChain original;
        private final List<BotMethodProcessor> processors;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return new VirtualBotMethodProcessorChain(original, processors).invoke(proxy, method, args);
        }
    }

    @PlatformProcessor
    @RequiredArgsConstructor
    private class BotThreadScopedContextMethodHolder implements BotMethodProcessor {

        private final BotData botData;

        public Object invoke(Object proxy, Method method, Object[] args, BotMethodProcessorChain chain) {
            try {
                apiContextHandler.create(TransformDataToContextArgumentProcessor.transformDataToContext(botData));
                return chain.invoke(proxy, method, args);
            } finally {
                apiContextHandler.delete();
            }
        }

        public int getOrder() {
            return BotProcessorDefinitions.THREAD_LOCAL_PRIORITY;
        }
    }

    @PlatformProcessor
    @RequiredArgsConstructor
    private static class BotArgumentScopedContextMethodHolder implements BotMethodProcessor {

        private final BotArgumentProcessor argumentProcessor;
        private final BotData botData;

        public Object invoke(Object proxy, Method method, Object[] args, BotMethodProcessorChain chain) {
            argumentProcessor.setArguments(botData, args);
            return chain.invoke(proxy, method, args);
        }

        public int getOrder() {
            return BotProcessorDefinitions.ARGUMENT_LOCAL_PRIORITY;
        }
    }

    @Autowired
    void setBotMethodProcessors(@Nullable List<BotMethodProcessor> botMethodProcessors) {
        if (botMethodProcessors != null)
            this.botMethodProcessors = botMethodProcessors;
    }

}
