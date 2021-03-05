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

package com.goodboy.telegram.bot.spring.api.meta;

import com.goodboy.telegram.bot.spring.api.gateway.GatewayRoutingResolver;
import com.goodboy.telegram.bot.spring.api.token.TelegramApiTokenProvider;
import com.goodboy.telegram.bot.spring.impl.gateway.UniformWeightGatewayRoutingResolver;
import com.goodboy.telegram.bot.spring.impl.token.SpringEnvironmentTelegramApiTokenProvider;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@AmIBot
@RestController
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping
public @interface Bot {

    /**
     * Unique bot name on spring container space
     *
     * @return bot name
     */
    @AliasFor(annotation = RestController.class)
    String value() default "";

    /**
     * @return bot name
     */
    @AliasFor(value = "value", annotation = RequestMapping.class)
    String[] mapping() default "";

    /**
     * @return request mapping
     */
    @AliasFor(attribute = "value", annotation = RequestMapping.class)
    String[] path() default "";

    /**
     * @return token provider class
     */
    Class<? extends TelegramApiTokenProvider> apiTokenProvider() default SpringEnvironmentTelegramApiTokenProvider.class;

    /**
     * @return routing calculator
     */
    Class<? extends GatewayRoutingResolver> apiGatewayRoutingResolver() default UniformWeightGatewayRoutingResolver.class;

    /**
     * @return proxy type
     */
    ProxyType proxyType() default ProxyType.THREAD_SCOPE;

    //todo polling here - полинг это способ получения дополнительных данных для контроллера а не отдельный вид среза внутри

    enum ProxyType{
        /**
         * Контекстные данные будет возможно достать из {@link ThreadLocal} в который процессор аннотации сложит его
         * Данный скоуп более широкий, чем {@link this#ARGUMENT_SCOPE} и включает его в себя
         */
        THREAD_SCOPE,
        /**
         * Контекстные данные будет возхможно получить на вход в метод в виде аргумента
         */
        ARGUMENT_SCOPE
    }
}
