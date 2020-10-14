package com.goodboy.telegram.bot.spring;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Data
@Accessors(chain = true)
public class WebhookBeanDefinition {

    /**
     * unique bot name on spring container space
     */
    private String botName;

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
     * The token is a string along the lines of 110201543:AAHdqTcvCH1vGWJxfSeofSAs0K5PALDsaw that is required
     * to authorize the bot and send requests to the Bot API. Keep your token secure and store it safely,
     * it can be used by anyone to control your bot.
     */
    private TokenHandler tokenHandler;

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

    public enum RegistryBot {
        NON, REGISTRY_ON_MISSING, REWRITE
    }
}
