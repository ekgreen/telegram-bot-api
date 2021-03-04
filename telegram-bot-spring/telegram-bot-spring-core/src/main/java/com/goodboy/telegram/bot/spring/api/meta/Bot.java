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

@AmIBot
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestController
@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
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
