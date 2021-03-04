package com.goodboy.telegram.bot.spring.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ServletTelegramApi {

    @AliasFor(annotation = Qualifier.class)
    String value() default "";

    /**
     * Constant for get updates servlet
     */
    public final static String GET_UPDATE_TELEGRAM_API_SERVLET = "getUpdateTelegramApiServlet";
}
