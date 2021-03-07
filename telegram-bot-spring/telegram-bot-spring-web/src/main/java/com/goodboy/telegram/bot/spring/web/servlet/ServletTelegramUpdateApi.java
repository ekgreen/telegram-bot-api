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

package com.goodboy.telegram.bot.spring.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.events.OnBotRegistry;
import com.goodboy.telegram.bot.spring.api.gateway.Gateway;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.goodboy.telegram.bot.spring.web.servlet.ServletTelegramApi.GET_UPDATE_TELEGRAM_API_SERVLET;


/**
 * Не сложная реализация сервлета, без миллиона обворочек {@link org.springframework.web.servlet.DispatcherServlet}
 * которая умеет маршрутизировать запросы к нужному боту и имеет некоторые особенности:
 * - ветвление происходит не только по пути (path), но и по командам (command)
 * <p>
 * Авторизацию мы также не будем прикручивать к данному сервлету так как это не имеет никакого смысла, мы выставляем
 * публичное API наружу и ожидаем что нас будет вызывать Telegram без дополнительной аутентификации и авторизации
 * <p>
 * Для прочих своих эндпоинтов продолжайте пользоваться мощным средство Spring {@link org.springframework.web.servlet.DispatcherServlet}
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@Infrastructure
@ServletTelegramApi(GET_UPDATE_TELEGRAM_API_SERVLET)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ServletTelegramUpdateApi extends HttpServlet implements OnBotRegistry {

    // simple rout from path to bot name
    private final Map<String, String> routes = new HashMap<>();
    // bots api gateway
    private final Gateway gateway;
    // input stream de/marshaller
    private final ObjectMapper mapper;
    // root server context
    @Value("${server.servlet.context-path:#{null}}")
    private String serverServletContextPath;
    // root telegram servlet context
    @Value("${telegram.servlet.context-path}")
    private String telegramApiServletContextPath;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 1. resolve bot name for gateway
            final String uri = request.getRequestURI();

            if (!routes.containsKey(uri)) {
                response.sendError(404, "no routes found");
                return;
            }

            final String botName = routes.get(uri);

            // 2. get update from request
            final Update update = mapper.readValue(request.getInputStream(), Update.class);

            if (update == null) {
                response.sendError(400, "no body");
                return;
            }

            gateway.routing(botName, update);
        } catch (Exception exception) {
            // todo code error resolver
            log.warn("service not available", exception);
            response.sendError(500, "unavailable");
        }
    }


    @Override
    public void onRegistry(@NotNull BotData data) {
        final String botName = data.getName();
        final String[] paths = data.getPaths();

        Arrays.stream(paths).forEach(path -> routes.put((serverServletContextPath != null ? serverServletContextPath : "") + telegramApiServletContextPath + path, botName));
    }
}
