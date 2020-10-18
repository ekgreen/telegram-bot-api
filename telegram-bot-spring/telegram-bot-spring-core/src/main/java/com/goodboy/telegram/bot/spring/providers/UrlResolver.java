package com.goodboy.telegram.bot.spring.providers;

public interface UrlResolver {

    /**
     * Returns context root url
     *
     * For example: http://127.0.0.1:80/telegram/bot/***
     *
     * @return url
     */
    public String getContextUrl(String ... paths);

    /**
     * Returns context root path
     *
     * For example: /telegram/bot/***
     *
     * @return url
     */
    public String getContextPath(String ... paths);
}
