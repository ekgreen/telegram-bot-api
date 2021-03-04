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
@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
public @interface ApiHook {

    /**
     * The primary mapping expressed by this annotation.
     * <p>This is an alias for {@link #path}.
     * <p>
     * For example:
     * {@code @WebhookApi("/foo")} is equivalent to
     * {@code @WebhookApi(path="/foo")}.
     */
    @AliasFor(value = "path", annotation = RequestMapping.class)
    String[] value() default StringUtils.EMPTY;

    /**
     * The path mapping URIs (e.g. {@code "/profile"})
     */
    @AliasFor(annotation = RequestMapping.class)
    String[] path() default StringUtils.EMPTY;

    /**
     * The HTTP request methods to map
     */
    @AliasFor(annotation = RequestMapping.class)
    RequestMethod[] method() default {RequestMethod.POST};

    /**
     * The commands which supports webhook (part of API Telegram)
     */
    String[] command() default StringUtils.EMPTY;

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
