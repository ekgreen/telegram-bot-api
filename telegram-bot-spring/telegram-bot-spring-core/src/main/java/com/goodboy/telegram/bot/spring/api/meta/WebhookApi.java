package com.goodboy.telegram.bot.spring.api.meta;

import com.goodboy.telegram.bot.spring.impl.providers.ApplicationPropertyCertificateProvider;
import com.goodboy.telegram.bot.spring.api.providers.CertificateProvider;
import org.apache.commons.lang3.StringUtils;
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
     * Self registry bot use telegram bot api and request telegram
     * to registry bots webhook with other params below.
     * <p>
     * All other params of current annotation using exclusively for bot registration.
     * <p>
     * Do not fill fields if {@see WebhookApi#selfRegistryBot()} is false
     */
    WebhookBeanDefinition.RegistryBot selfRegistryHook() default WebhookBeanDefinition.RegistryBot.NON;

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
