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

package com.goodboy.telegram.bot.spring.impl.processors.arguments;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.meta.Webhook;
import com.goodboy.telegram.bot.spring.api.processor.arguments.BotArgumentProcessor;
import com.goodboy.telegram.bot.spring.api.processor.arguments.BotArgumentProcessorFactory;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.processor.arguments.TypeArgumentProcessor;
import com.goodboy.telegram.bot.http.api.client.response.UpdateProvider;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@Infrastructure
public class BaseBotArgumentProcessorFactory implements BotArgumentProcessorFactory {

    private final static int DEFAULT_UPDATE_POSITION = -1;

    private final UpdateProvider updateProvider;

    private final Map<Class<?>, TypeArgumentProcessor<?>> typeProcessors;

    @Autowired
    public BaseBotArgumentProcessorFactory(
            Optional<UpdateProvider> updateProvider,
            List<TypeArgumentProcessor<?>> processors) {
        this.updateProvider = updateProvider.orElseGet(() -> () -> null);

        final Map<Class<?>, TypeArgumentProcessor<?>> typeProcessors = new HashMap<>();

        for (TypeArgumentProcessor<?> processor : processors) {
            final Class<?> type = processor.supportedType();

            if (typeProcessors.containsKey(type))
                throw new IllegalStateException("key already exists in map = " + type);

            typeProcessors.put(type, processor);
        }

        this.typeProcessors = ImmutableMap.copyOf(typeProcessors);
    }

    public @Nonnull
    BotArgumentProcessor createArgumentProcessor(@Nonnull Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        final List<PositionArgumentProcessor> argumentProcessors = IntStream.range(0, parameterTypes.length)
                .mapToObj(p -> createArgumentHandlerOrSkip(parameterTypes[p], parameterAnnotations[p], p))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (argumentProcessors.size() == 0)
            return NoArgumentProcessor.INSTANCE;

        return new EachArgumentProcessor(findUpdatePosition(parameterTypes), defineIsWebhookType(method), argumentProcessors);
    }

    private <T> PositionArgumentProcessor createArgumentHandlerOrSkip(Class<T> type, Annotation[] parameterAnnotation, int position) {
        TypeArgumentProcessor<?> processor = typeProcessors.get(type);

        // не нашли по типу
        if (processor == null) {
            for (Annotation annotation : parameterAnnotation) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();

                final boolean annotationProcessorFound = typeProcessors.containsKey(annotationType);

                if (annotationProcessorFound) {
                    processor = typeProcessors.get(annotationType);
                    break;
                }
            }
        }

        return processor != null ? new PositionArgumentProcessor(position, type, processor) : null;
    }

    private int findUpdatePosition(Class<?>[] parameterTypes) {

        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];

            if (Update.class.isAssignableFrom(parameterType))
                return i;
        }

        return DEFAULT_UPDATE_POSITION;
    }

    private boolean defineIsWebhookType(@Nonnull Method method) {
        return Arrays
                .stream(method.getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .collect(Collectors.toSet())
                .contains(Webhook.class); // переделать на общий конфиг?
    }

    @RequiredArgsConstructor
    private class EachArgumentProcessor implements BotArgumentProcessor {

        private final int updatePosition;
        private final boolean isWebhook;

        private final List<PositionArgumentProcessor> argumentProcessors;

        @Override
        public void setArguments(@NotNull BotData botData, Object[] arguments) {
            final Update request = isWebhook ?
                    workWithUpdate(botData, arguments)
                    :
                    null;

            argumentProcessors.forEach(processor -> processor.setArgument(arguments, botData, request));
        }

        private Update workWithUpdate(@NotNull BotData botData, Object[] arguments) {
            final Update request;

            if (updatePosition >= 0 && arguments[updatePosition] != null)
                request = (Update) arguments[updatePosition];
            else
                request = updateProvider.getUpdate();

            // засетим аргмент
            if (updatePosition >= 0)
                arguments[updatePosition] = request;

            log.info("bots [{}] webhook {} received a message [update] -> {}", botData.getName(), botData.getPaths(), request);

            return request;
        }
    }

    private final static class NoArgumentProcessor implements BotArgumentProcessor {

        private final static BotArgumentProcessor INSTANCE = new NoArgumentProcessor();

        @Override
        public void setArguments(@NotNull BotData botData, @NotNull Object[] arguments) {
            /*NoN*/
        }
    }

    @RequiredArgsConstructor
    private static class PositionArgumentProcessor {

        private final int argumentPosition;
        private final Class<?> argumentType;

        private final TypeArgumentProcessor<?> processor;

        /**
         * Проставляем аргумент определенного типа
         * На вход в метод передаются основные компоненты, которые могу повлиять на динамизм проставления аргументов, а именно
         * данные о боте {@link BotData} + информация о запросе {@link Update}
         *
         * @param arguments перечень всех аргументов метода
         * @param botData   данные о боте
         * @param update    данные о запросе
         */
        public void setArgument(@Nonnull Object[] arguments, @Nonnull BotData botData, @Nullable Update update) {
            processor.setArgument(argumentPosition, argumentType, arguments, botData, update);
        }
    }

}
