package com.goodboy.telegram.bot.spring.autoconfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.spring.api.ServletTelegramApi;
import com.goodboy.telegram.bot.spring.api.gateway.Gateway;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayFilter;
import com.goodboy.telegram.bot.spring.impl.gateway.TelegramBotApiGateway;
import com.goodboy.telegram.bot.spring.impl.servlet.ServletTelegramUpdateApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServlet;
import java.util.List;

import static com.goodboy.telegram.bot.spring.api.ServletTelegramApi.GET_UPDATE_TELEGRAM_API_SERVLET;

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
