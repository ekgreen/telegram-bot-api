package com.goodboy.telegram.bot.spring.api.processor.barrier;

import com.goodboy.telegram.bot.spring.api.data.BotData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public interface OriginChainWildcardFactory {

    /**
     * Return True if return type must have additional logic concerning on origin chain execution
     * after some actions
     *
     * Example:
     * Webhook methods have to handle chat actions in heavyweight operations it means that webhook have to get
     * data from origin chain (fast action), furthermore if overall method result returns api and origin chain have
     * to handle it - origin chain have to find out about it somehow
     * In fact origin chain wildcard aimed at guaranteeing the execution of the origin chain if it requires
     *
     * webhook <--> wildcard[fast action]- barrier <- origin.invoke
     *                  |
     *                  V
     *             origin.sendApi
     *
     * @param returnType return value of evaluation method
     * @return           origin return type key or null
     */
    public boolean isWildcardAvailable(@NotNull Class<?> returnType);

    /**
     * In fact, it is annotation factory which wrap return method type in invocation after call proxy
     *
     * Wildcard chain guarantees origin chain execution
     *
     * @param botData    data about bot
     * @param returnType return value of evaluation method
     * @return wildcard proxy builder
     */
    public <T> Wildcard<T> createWildcard(
            @Nonnull BotData botData,
            @Nonnull Class<T> returnType
    );
}
