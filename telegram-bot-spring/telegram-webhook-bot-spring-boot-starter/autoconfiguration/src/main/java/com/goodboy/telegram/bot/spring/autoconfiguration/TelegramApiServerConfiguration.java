package com.goodboy.telegram.bot.spring.autoconfiguration;

import com.goodboy.telegram.bot.spring.api.servlet.ServletTelegramApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;

import static com.goodboy.telegram.bot.spring.api.servlet.ServletTelegramApi.GET_UPDATE_TELEGRAM_API_SERVLET;

@Configuration
public class TelegramApiServerConfiguration {

    @Value("${telegram.servlet.context-path}")
    private String telegramApiServletContextPath;

    @Bean
    public ServletRegistrationBean<HttpServlet> stateServlet(
            @ServletTelegramApi(GET_UPDATE_TELEGRAM_API_SERVLET) HttpServlet servlet
    ) {
        final var bean = new ServletRegistrationBean<HttpServlet>();
        bean.setServlet(servlet);
        bean.addUrlMappings(telegramApiServletContextPath + "/*");
        bean.setLoadOnStartup(1);
        return bean;
    }

}
