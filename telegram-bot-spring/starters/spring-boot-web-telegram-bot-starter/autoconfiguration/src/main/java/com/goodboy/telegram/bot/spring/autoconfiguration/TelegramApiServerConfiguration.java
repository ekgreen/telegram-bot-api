/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.spring.autoconfiguration;

import com.goodboy.telegram.bot.spring.web.servlet.ServletTelegramApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;

import static com.goodboy.telegram.bot.spring.web.servlet.ServletTelegramApi.GET_UPDATE_TELEGRAM_API_SERVLET;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
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
