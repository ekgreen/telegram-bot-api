package com.goodboy.telegram.bot.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 *
 *
 */
@Servlet
public class TelegramBotServlet extends HttpServlet implements BeanPostProcessor {

//    private final Map<String, Webhook> s;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public String resolveUrl() {

        return null;
    }


}
