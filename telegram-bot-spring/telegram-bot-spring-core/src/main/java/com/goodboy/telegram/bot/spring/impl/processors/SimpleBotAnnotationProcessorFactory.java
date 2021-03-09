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

import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.processor.BotAnnotationProcessor;
import com.goodboy.telegram.bot.spring.api.processor.BotAnnotationProcessorFactory;
import com.goodboy.telegram.bot.spring.api.processor.BotMethodProcessorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Primary
@Infrastructure
public class SimpleBotAnnotationProcessorFactory implements BotAnnotationProcessorFactory {

    private final Predicate<Method> supportsCondition;
    private final Function<Method, BotAnnotationProcessor> resolver;

    @Autowired
    public SimpleBotAnnotationProcessorFactory(@Nonnull List<BotAnnotationProcessor> processors) {
        final Set<Class<? extends Annotation>> annotations = new HashSet<>();

        for (BotAnnotationProcessor processor : processors) {
            var type = processor.supports();

            if (annotations.contains(type))
                throw new IllegalArgumentException("annotation processor [" + type.getSimpleName() + "] already registered in annotation factory");

            annotations.add(type);
        }

        var mirror = processors.stream().collect(toMap(
                BotAnnotationProcessor::supports,
                p -> p
        ));

        this.supportsCondition = (method) -> Arrays.stream(method.getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(annotations::contains);

        this.resolver = (method) -> {
            if(supportsCondition.test(method)) {
                final Annotation[] methodDeclaredAnnotations = method.getDeclaredAnnotations();

                for (Annotation methodDeclaredAnnotation : methodDeclaredAnnotations) {
                    var type = methodDeclaredAnnotation.annotationType();

                    if (mirror.containsKey(type))
                        return mirror.get(type);
                }

                throw new IllegalArgumentException("non consistent bot platform api!");
            }

            throw new IllegalArgumentException("non any annotation processor provides handler for current method [" + method + "]");
        };
    }

    public @Nonnull
    BotMethodProcessorChain createAnnotationProcessor(@Nonnull BotData botData, @Nonnull Method method) {
        return resolver.apply(method).createAnnotationProcessor(botData, method);
    }

    public boolean supports(Method method) {
        return supportsCondition.test(method);
    }
}
