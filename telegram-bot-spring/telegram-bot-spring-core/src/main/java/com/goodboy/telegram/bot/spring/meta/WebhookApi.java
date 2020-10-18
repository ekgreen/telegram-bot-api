package com.goodboy.telegram.bot.spring.meta;

import com.goodboy.telegram.bot.spring.providers.ApplicationPropertyCertificateProvider;
import com.goodboy.telegram.bot.spring.providers.ApplicationPropertyTokenHandler;
import com.goodboy.telegram.bot.spring.providers.CertificateProvider;
import com.goodboy.telegram.bot.spring.providers.TokenHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Service
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface WebhookApi {

    /**
     * The primary mapping expressed by this annotation.
     * <p>This is an alias for {@link #path}.
     * <p>
     * For example:
     * {@code @WebhookApi("/foo")} is equivalent to
     * {@code @WebhookApi(path="/foo")}.
     */
    @AliasFor("path")
    String value() default StringUtils.EMPTY;

    /**
     * The path mapping URIs (e.g. {@code "/profile"})
     */
    @AliasFor("value")
    String path() default StringUtils.EMPTY;

    /**
     * unique bot name on spring container space
     */
    String bot() default Strings.EMPTY;

    /**
     * Self registry bot use telegram bot api and request telegram
     * to registry bots webhook with other params below.
     * <p>
     * All other params of current annotation using exclusively for bot registration.
     * <p>
     * Do not fill fields if {@see WebhookApi#selfRegistryBot()} is false
     */
    WebhookBeanDefinition.RegistryBot selfRegistryBot() default WebhookBeanDefinition.RegistryBot.NON;

    /**
     * The token is a string along the lines of 110201543:AAHdqTcvCH1vGWJxfSeofSAs0K5PALDsaw that is required
     * to authorize the bot and send requests to the Bot API. Keep your token secure and store it safely,
     * it can be used by anyone to control your bot.
     */
    String token() default StringUtils.EMPTY;

    /**
     * Non-bean implementation of {@see TokenHandler} which used for
     * more completed way than {@link this#token()}.
     * <p>
     * For example: use it to read bot token from a file
     */
    Class<? extends TokenHandler> tokenHandler() default ApplicationPropertyTokenHandler.class;

    /**
     * Non-bean implementation of {@see TokenHandler} which used for
     * more completed way than {@link this#token()}.
     * Use it when you enjoy
     * <p>
     * For example: use it to read bot token from a file
     */
    String tokenHandlerBean() default StringUtils.EMPTY;

    /**
     * Uploading your public key certificate so that the root certificate in use can be checked
     */
    Class<? extends CertificateProvider> certificate() default ApplicationPropertyCertificateProvider.class;

    /**
     * Maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100.
     * Defaults to 40. Use lower values to limit the load on your bot's server, and higher values to increase
     * your bot's throughput.
     * <p>
     * If parameter remains -1 value telegram http client setup connection automatically
     */
    int maxConnections() default -1;
}
