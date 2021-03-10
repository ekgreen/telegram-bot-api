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

package com.goodboy.telegram.bot.spring.impl.processors.origin;

import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.http.api.client.context.TelegramApiContextResolver;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.processor.BotAnnotationProcessorFactory;
import com.goodboy.telegram.bot.spring.api.processor.BotMethodProcessorChain;
import com.goodboy.telegram.bot.spring.api.sender.ApiMethodExecutor;
import com.goodboy.telegram.bot.spring.impl.processors.arguments.TransformDataToContextArgumentProcessor;
import com.goodboy.telegram.bot.spring.api.processor.barrier.OriginChainBarrier;
import com.goodboy.telegram.bot.spring.api.processor.barrier.OriginChainWildcardFactory;
import com.goodboy.telegram.bot.spring.api.processor.barrier.Wildcard;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Origin chain based on user api-responses execution
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@OriginFactory
@RequiredArgsConstructor
public class OriginBotAnnotationProcessorFactory implements BotAnnotationProcessorFactory {

    private final ApiMethodExecutor executor;
    private final TelegramApiContextResolver updateContextResolver;
    private final OriginChainBarrier originChainBarrierFactory;
    private final OriginChainWildcardFactory originChainWildcardFactory;

    public @Nonnull
    BotMethodProcessorChain createAnnotationProcessor(@Nonnull BotData botData, @Nonnull Method method) {
        return createResponseWrapperForWildcardSupportedApi(botData, method);
    }

    private @Nonnull
    BotMethodProcessorChain createResponseWrapperForWildcardSupportedApi(@Nonnull BotData botData, @Nonnull Method method) {
        // get return type
        final Class<?> returnType = method.getReturnType();

        // is chain returns value [part of origin chain]
        final boolean notUnderArmor = originChainBarrierFactory.isNotUnderArmor(returnType);

        // is wildcard proxy available
        final boolean wildcardAvailable = notUnderArmor && originChainWildcardFactory.isWildcardAvailable(returnType);

        // create update context selector
        final Function<Object[], UpdateContext> contextSelector = wildUpdateContextSelector(botData, method);

        // if return type assignable from Api - FixedExecutableOriginBotAnnotationProcessor
        if(Api.class.isAssignableFrom(returnType))
            return new FixedExecutableOriginBotAnnotationProcessor(method, contextSelector);

        // if wildcard proxy available create - WildcardExecutableOriginBotAnnotationProcessor
        if(wildcardAvailable){
            return new WildcardExecutableOriginBotAnnotationProcessor(
                    method,
                    originChainWildcardFactory.createWildcard(botData, returnType),
                    contextSelector
            );
        }

        // or else return default chain - OriginBotAnnotationProcessor
        return new OriginBotAnnotationProcessor(method);
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
    @RequiredArgsConstructor
    public static class OriginBotAnnotationProcessor implements BotMethodProcessorChain {

        private final Method origin;

        @SneakyThrows
        public Object invoke(Object proxy, Method method, Object[] args) {
            origin.invoke(proxy, args);

            return null; // never return response, it have to be void (or null)
        }
    }

    @RequiredArgsConstructor
    public class FixedExecutableOriginBotAnnotationProcessor implements BotMethodProcessorChain {

        private final Method origin;
        private final Function<Object[], UpdateContext> updateContextResolver;

        @SneakyThrows
        public Object invoke(Object proxy, Method method, Object[] args) {
            final Object invoke = origin.invoke(proxy, args);

            if (invoke != null)
                executor.sendApi(updateContextResolver.apply(args), (Api) invoke);

            return null; // never return response, it have to be void (or null)
        }
    }

    @RequiredArgsConstructor
    public class WildcardExecutableOriginBotAnnotationProcessor implements BotMethodProcessorChain {

        private final Method origin;

        private final Wildcard wildcard;
        private final Function<Object[], UpdateContext> updateContextResolver;

        @SneakyThrows
        public Object invoke(Object proxy, Method method, Object[] args) {
            Object invoke = origin.invoke(proxy, args);

            return wildcard.doWild(invoke, (api) -> executor.sendApi(updateContextResolver.apply(args), api));
        }
    }

}
