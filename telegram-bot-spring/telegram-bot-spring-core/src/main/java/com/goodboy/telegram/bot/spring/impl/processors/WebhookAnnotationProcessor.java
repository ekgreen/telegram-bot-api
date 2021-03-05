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

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
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
