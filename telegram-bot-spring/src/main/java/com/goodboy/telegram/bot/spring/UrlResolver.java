package com.goodboy.telegram.bot.spring;

public interface UrlResolver {

    /**
     * Returns context root url
     *
     * For example: http://127.0.0.1:80/telegram/bot
     *
     * @return url
     */
    public String getContextUrl();
}
