package com.goodboy.telegram.bot.api.meta;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Multipart {
    Class<? extends PropertyNamingStrategy.PropertyNamingStrategyBase> strategy() default PropertyNamingStrategy.SnakeCaseStrategy.class;
}
