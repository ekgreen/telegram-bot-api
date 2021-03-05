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

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Webhook {

    /**
     * The HTTP request methods to map
     */
    RequestMethod[] method() default {RequestMethod.POST};

    /**
     * The commands which supports webhook (part of API Telegram)
     */
    String[] command() default {};

    /**
     * Type of webhook execution
     */
    ExecutionType executionType() default ExecutionType.LIGHTWEIGHT;

    /**
     * @return name of bean for heavy weight execution or will executed in default
     */
    String heavyWeightExecutorService() default "";

    public enum ExecutionType{
        /**
         * means that method is light and execution might be on http life-connection
         */
        LIGHTWEIGHT,
        /**
         * means that method is unstable and execution type have to be calculated automatically
         */
        SNEAKY,
        /**
         * means that method is heavy and http connection should not hang
         */
        HEAVYWEIGHT
    }
}
