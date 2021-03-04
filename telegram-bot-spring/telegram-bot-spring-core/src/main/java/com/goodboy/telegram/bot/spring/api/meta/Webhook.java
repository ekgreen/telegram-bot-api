package com.goodboy.telegram.bot.spring.api.meta;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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