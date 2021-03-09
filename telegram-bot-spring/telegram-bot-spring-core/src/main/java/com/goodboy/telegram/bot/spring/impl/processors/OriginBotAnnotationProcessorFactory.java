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
import com.goodboy.telegram.bot.spring.api.actions.FastAction;
import com.goodboy.telegram.bot.spring.api.actions.Solo;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.processor.BotAnnotationProcessorFactory;
import com.goodboy.telegram.bot.spring.api.processor.BotMethodProcessorChain;
import com.goodboy.telegram.bot.spring.api.sender.ApiMethodExecutor;
import com.goodboy.telegram.bot.spring.impl.processors.arguments.TransformDataToContextArgumentProcessor;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@OriginFactory
public class OriginBotAnnotationProcessorFactory implements BotAnnotationProcessorFactory {

    private final Map<? extends Class<? extends Api>, ? extends ApiMethodExecutor<? extends Api>> executors;
    private final TelegramApiContextResolver updateContextResolver;

    @Autowired
    public OriginBotAnnotationProcessorFactory(@Nullable List<ApiMethodExecutor<? extends Api>> executors, TelegramApiContextResolver apiContextResolver) {
        this.executors = ImmutableMap.copyOf(Optional.ofNullable(executors)
                .map(l -> l.stream().collect(Collectors.toMap(
                        ApiMethodExecutor::type,
                        e -> e
                )))
                .orElseGet(HashMap::new));
        this.updateContextResolver = apiContextResolver;
    }

    public @Nonnull
    BotMethodProcessorChain createAnnotationProcessor(@Nonnull BotData botData, @Nonnull Method method) {
        return createResponseWrapperForWildcardSupportedApi(botData, method);
    }

    private @Nonnull
    BotMethodProcessorChain createResponseWrapperForWildcardSupportedApi(@Nonnull BotData botData, @Nonnull Method method) {
        // get return type
        final Class<?> returnType = method.getReturnType();



        // persist return type by supported operations enum
        SupportedWildcard wildcard = null;

        // api type for research api sender (api executor)
        final Class<?> api;

        // note: now supported only fast action if it will be clearly that types counting quantity
        // create dynamic loaders using spring framework
        // note: now it will be hardcode reaction on any specific return type

        // if return type is assignable to fast action - we have to wrap return answer in extra logic
        // note: we have to do that cuz fast action semantic return result immediately
        // it means that origin chain will not send heavyweight operation result
        // seems so extra logic is about sending result request
        if (FastAction.class.isAssignableFrom(returnType)) {

            // Solo fast action means that fast action return null
            if (Solo.class.isAssignableFrom(returnType))
                return createDefaultOriginChain(method);

            wildcard = SupportedWildcard.FAST_ACTION;

            // fast action generic is API
            api = (Class<?>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
        }

        // calculate api executor
        final ApiMethodExecutor<Api> executor = wildApiSelector(returnType);

        // if executor not found - extra logic not needed
        if (executor == null)
            return createDefaultOriginChain(method);
        ;

        // context selector
        final Function<Object[], UpdateContext> contextSelector = wildUpdateContextSelector(botData, method);

        if(wildcard == null)
            return new FixedExecutableOriginBotAnnotationProcessor(method, executor, contextSelector);

        // wildcard handler
        final WildcardHandler handler = createWildcardHandler(wildcard, executor);

        return new WildcardExecutableOriginBotAnnotationProcessor(method, handler, contextSelector);
    }

    private @Nonnull
    WildcardHandler<?> createWildcardHandler(@Nonnull SupportedWildcard wildcard, @Nonnull ApiMethodExecutor<Api> executor) {
        // todo rework switches
        if (wildcard == SupportedWildcard.FAST_ACTION)
            return (WildcardHandler<FastAction<Api>>) (origin, context) -> new OriginFastAction(origin, context, executor);

        throw new BeanCreationException("unsupported wildcard = " + wildcard);
    }

    private static enum SupportedWildcard{
        FAST_ACTION,
        ;
    }

    private static interface WildcardHandler<T> {
        // wrap object with extra logic
        public T wrap(T origin, UpdateContext context);

        // by default all origin chains does not provide values outside
        // BUT some wildcard types may produce it if necessary
        // for instance fast action it is event handling outside
        // origin chain just know that it must provide it with extra logic
        public default boolean returnValueOutside(){
            return true;
        }
    }

    @RequiredArgsConstructor
    private static class OriginFastAction implements FastAction<Api> {

        // origin fast action that returns
        private final FastAction<Api> origin;
        // context supplier
        private final UpdateContext context;
        // api executor
        private final ApiMethodExecutor<Api> executor;

        @Override
        public Api heavyweight() {
            // invoke origin method
            final Api heavyweight = origin.heavyweight();

            // execute api
            if (heavyweight != null)
                executor.handle(context, heavyweight);

            // return origin
            return heavyweight;
        }
    }

    private static BotMethodProcessorChain createDefaultOriginChain(@Nonnull Method method) {
        return new OriginBotAnnotationProcessor(method);
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
    Function<Object[], UpdateContext> wildUpdateContextSelector(BotData botData, Method method) {
        final int uploadContextArgument = findContextParameterId(method);
        final UpdateContext defaultUploadContext = TransformDataToContextArgumentProcessor.transformDataToContext(botData);

        return (args) -> {
            UpdateContext context = null;

            if (uploadContextArgument != -1)
                context = (UpdateContext) args[uploadContextArgument];

            // продолжаем поиск
            if (context == null)
                context = updateContextResolver.read();

            if(context == null)
                context = defaultUploadContext;

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
                executor.handle(updateContextResolver.apply(args), (Api) invoke);

            return null; // never return response, it have to be void (or null)
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class WildcardExecutableOriginBotAnnotationProcessor implements BotMethodProcessorChain {

        private final Method origin;

        private final WildcardHandler<Object> handler;
        private final Function<Object[], UpdateContext> updateContextResolver;

        @SneakyThrows
        public Object invoke(Object proxy, Method method, Object[] args) {
            Object invoke = origin.invoke(proxy, args);

            if (invoke != null){
                invoke = handler.wrap(invoke, updateContextResolver.apply(args));
            }

            return handler.returnValueOutside() ? invoke : null; // may produce value
        }
    }

}
