package com.goodboy.telegram.bot.spring.impl.processors;

import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.meta.Webhook;
import com.goodboy.telegram.bot.spring.api.processor.BotAnnotationProcessor;
import com.goodboy.telegram.bot.spring.api.processor.BotAnnotationProcessorFactory;
import com.goodboy.telegram.bot.spring.api.processor.BotMethodProcessor;
import com.goodboy.telegram.bot.spring.api.processor.BotMethodProcessorChain;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static java.lang.String.format;

@Infrastructure
public class WebhookAnnotationProcessor implements BotAnnotationProcessor {

    private final BotAnnotationProcessorFactory originFactory;

    @Autowired
    public WebhookAnnotationProcessor(@OriginFactory BotAnnotationProcessorFactory originFactory) {
        this.originFactory = originFactory;
    }

    public @Nonnull
    BotMethodProcessorChain createAnnotationProcessor(@Nonnull Method method) {
        if (!method.isAnnotationPresent(supports()))
            throw new IllegalArgumentException(format("attempt to create webhook annotation processor with non-annotated method [%s] by %s",
                    method.getName(), supports().getSimpleName()
            ));

        return originFactory.createAnnotationProcessor(method);
    }

    public Class<? extends Annotation> supports() {
        return Webhook.class;
    }

}
