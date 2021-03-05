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

package com.goodboy.telegram.bot.spring.impl.processors;

import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiContextResolver;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.spring.api.processor.BotAnnotationProcessorFactory;
import com.goodboy.telegram.bot.spring.api.processor.BotMethodProcessorChain;
import com.goodboy.telegram.bot.spring.api.sender.ApiMethodExecutor;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@OriginFactory
public class OriginBotAnnotationProcessorFactory implements BotAnnotationProcessorFactory {

    private final Map<? extends Class<? extends Api>, ? extends ApiMethodExecutor<? extends Api>> executors;
    private final TelegramApiContextResolver apiContextResolver;

    @Autowired
    public OriginBotAnnotationProcessorFactory(@Nullable List<ApiMethodExecutor<? extends Api>> executors, TelegramApiContextResolver apiContextResolver) {
        this.executors = ImmutableMap.copyOf(Optional.ofNullable(executors)
                .map(l -> l.stream().collect(Collectors.toMap(
                        ApiMethodExecutor::type,
                        e -> e
                )))
                .orElseGet(HashMap::new));
        this.apiContextResolver = apiContextResolver;
    }

    public @Nonnull
    BotMethodProcessorChain createAnnotationProcessor(@Nonnull Method method) {
        // 1. получаем возвращаемый тип реального метода
        final Class<?> returnType = method.getReturnType();

        // 2. вычисляем исполнитель метода в зависимости от возвращаемого API
        final ApiMethodExecutor<Api> executor = wildApiSelector(returnType);

        return executor == null ? new OriginBotAnnotationProcessor(method) :
                new FixedExecutableOriginBotAnnotationProcessor(method, executor, wildUpdateContextSelector(method));
    }

    private @Nullable
    ApiMethodExecutor<Api> wildApiSelector(Class<?> apiType) {
        if (!Api.class.isAssignableFrom(apiType)) {
            log.info("api executors have not be set - return type not API { type = {} }", apiType);
            return null;
        }

        if (apiType.isInterface()) {
            log.info("api method executors could not be found  - interfaces not supported { interface_type = {} }; " +
                    "method will return wild executor which try to find 'real' executor on runtime", apiType);

            // создадим кастомный экзекьютор, который на лету будет пытаться определить реальный экзекьютор
            return new ApiMethodExecutor<>() {

                public void handle(@NotNull UpdateContext context, @NotNull Api api) {
                    final ApiMethodExecutor<Api> executor = apiSelector(api.getClass());

                    if (executor != null)
                        executor.handle(context, api);
                }

                public @Nonnull
                Class<Api> type() {
                    return Api.class;
                }
            };
        }

        return apiSelector(apiType);
    }

    /**
     * Поиск экзекьютора, который бы исполнил вызов API Telegram
     *
     * @param apiType поддерживаемый тип
     * @return экзекьютор или null - если не найден
     */
    private @Nullable
    ApiMethodExecutor<Api> apiSelector(Class<?> apiType) {
        if (!executors.containsKey(apiType)) {
            log.warn("api method executors could not be found  - unsupported type { type = {} }", apiType);
            return null;
        }

        return (ApiMethodExecutor<Api>) executors.get(apiType); // definitely found
    }

    private @Nonnull
    Function<Object[], UpdateContext> wildUpdateContextSelector(Method method) {
        final int uploadContextArgument = findContextParameterId(method);

        return (args) -> {
            UpdateContext context = null;

            if (uploadContextArgument != -1)
                context = (UpdateContext) args[uploadContextArgument];

            // продолжаем поиск
            if (context == null)
                context = apiContextResolver.read();

            // если не нашли, тогда вернем протухший контекст
            if (context == null)
                context = RottenUploadContext.INSTANCE;

            return context;
        };
    }

    private int findContextParameterId(Method method) {
        Class<?>[] types = method.getParameterTypes();

        for (int p = 0; p < types.length; p++) {
            Class<?> parameterType = types[p];

            if (UpdateContext.class.isAssignableFrom(parameterType)) {
                return p;
            }
        }

        return -1;
    }

    /**
     * На этапе компиляции удалось определить возвращаемый тип API и API не поддерживается
     */
    @Slf4j
    @RequiredArgsConstructor
    public static class OriginBotAnnotationProcessor implements BotMethodProcessorChain {

        private final Method origin;

        @SneakyThrows
        public Object invoke(Object proxy, Method method, Object[] args) {
            origin.invoke(proxy, args);

            return null; // never return response, it have to be void (or null)
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class FixedExecutableOriginBotAnnotationProcessor implements BotMethodProcessorChain {

        private final Method origin;

        private final ApiMethodExecutor<Api> executor;
        private final Function<Object[], UpdateContext> updateContextResolver;

        @SneakyThrows
        public Object invoke(Object proxy, Method method, Object[] args) {
            final Object invoke = origin.invoke(proxy, args);

            if (invoke != null)
                try {
                    executor.handle(updateContextResolver.apply(args), (Api) invoke);
                }catch (Exception exception){
                    log.warn("api method executor can not apply request", exception);
                }

            return null; // never return response, it have to be void (or null)
        }
    }

}
