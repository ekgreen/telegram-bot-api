package com.goodboy.telegram.bot.spring.api.meta;

import com.goodboy.telegram.bot.spring.api.providers.CertificateProvider;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.Supplier;

import static com.goodboy.telegram.bot.spring.api.meta.WebhookBeanDefinition.RegistryStatus.NOT_OBTAINED;

@Data
@Accessors(chain = true)
public class WebhookBeanDefinition {

    /**
     * the path mapping URIs
     */
    private String path;

    /**
     * Self registry bot use telegram bot api and request telegram
     * to registry bots webhook with other params
     */
    private RegistryBot selfRegistry = RegistryBot.NON;

    /**
     * Path to Uploading your public key certificate so that the root certificate in use can be checked
     */
    private CertificateProvider certificateProvider;

    /**
     * Maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100.
     * Defaults to 40. Use lower values to limit the load on your bot's server, and higher values to increase
     * your bot's throughput.
     *
     * If parameter remains -1 value telegram http client setup connection automatically
     */
    private Integer maxConnections;

    /**
     * If {@link this#selfRegistry} != NON bot should self registry in telegram
     * Use this method to properly understand registry status. By default, supplier will
     * return status that ticket not obtained by registry service
     */
    private volatile Supplier<RegistryStatus> registryStatusPules = () -> NOT_OBTAINED;

    @Getter
    @RequiredArgsConstructor
    public enum RegistryStatus{
        NOT_OBTAINED(false), IN_PROCESS(false), ERROR(true), REGISTERED(true);

        private final boolean isFinalStatus;
    }

    public enum RegistryBot {
        NON, REGISTRY_ON_MISSING, REWRITE
    }
}
