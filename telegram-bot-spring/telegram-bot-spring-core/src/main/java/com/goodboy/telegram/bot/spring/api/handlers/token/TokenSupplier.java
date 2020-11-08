package com.goodboy.telegram.bot.spring.api.handlers.token;


import org.jetbrains.annotations.NotNull;

public interface TokenSupplier {

    /**
     * Get current request (callback) token
     *
     * @return token
     */
    String getRequestToken();

    /**
     * Get token by bot name
     *
     * @return token
     */
    String getTokenByName(@NotNull String botName);

    /**
     * Get token by bot request path
     *
     * @return token
     */
    String getTokenByPath(@NotNull String path);
}
