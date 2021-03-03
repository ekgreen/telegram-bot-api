package com.goodboy.telegram.bot.spring.impl.processors;

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
    BotMethodProcessorChain createAnnotationProcessor(@Nonnull Method method) {
        return resolver.apply(method).createAnnotationProcessor(method);
    }

    public boolean supports(Method method) {
        return supportsCondition.test(method);
    }
}
