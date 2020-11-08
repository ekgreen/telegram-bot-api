package com.goodboy.telegram.bot.spring.impl.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.spring.api.bot.Bot;
import com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition;
import com.goodboy.telegram.bot.spring.api.listeners.OnBotCreationListener;
import com.goodboy.telegram.bot.spring.api.meta.Servlet;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanCreationException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition.RegistryBot.NON;
import static com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition.RegistryStatus.REGISTERED;

/**
 *
 */
@Slf4j
@Servlet
@RequiredArgsConstructor
public class TelegramBotServlet extends HttpServlet implements OnBotCreationListener {

    private final ObjectMapper mapper;
    private final Map<String, Object> requestHandlers = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        // simple realisation of http servlet without any tinsel

        // 1) get path from http request
        final String path = req.getPathInfo();

        // 2) search handler by path
        if (requestHandlers.containsKey(path)) {
            // 3) path resolver by handler type
            callOnUntyped(requestHandlers.get(path)).accept(req, resp);
            return;
        }

        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "not found any handler for url [" + path + "]");
    }

    @Override
    public void onBotCreation(final @NotNull Bot bot, @NotNull BotBeanDefinition definition) {
        // 1) Define path. wildcards/expressions not supported
        final String path = definition.getPath();

        // 2) Validate path on uniqueness
        if (requestHandlers.containsKey(path)) {
            log.error("Attempt to registry two (or more) objects on single unique path { path = {}, new_handler = {}, exist_handler = {} }", path, webhook.getClass().getSimpleName(), requestHandlers.get(path).getClass().getSimpleName());
            throw new BeanCreationException("uniqueness violation. webhook registry in servlet container exception");
        }

        // 3) Create proxy for self registry webhooks
        @NotNull Bot candidate = webhook;

        if (definition.getSelfRegistry() != NON) {

            candidate = update -> {
                var status = definition.getRegistryStatusPules().get();

                if (status == REGISTERED) {
                    webhook.onUpdate(update);
                    return;
                }

                // probably hack case, when request coming not form registry server
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "webhook not initialised yet [" + definition.getBotName() + "]");
            };
        }

        requestHandlers.put(path, candidate);
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> callOnUntyped(@NotNull Object handler) {
        if (handler instanceof Bot)
            return new WebhookBiConsumer((Bot) handler, mapper);

        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "unknown handler [" + handler.getClass().getSimpleName() + "] type");
    }

    @RequiredArgsConstructor
    private static class WebhookBiConsumer implements BiConsumer<HttpServletRequest, HttpServletResponse> {

        private final Bot webhook;
        private final ObjectMapper mapper;

        @SneakyThrows
        public void accept(HttpServletRequest request, HttpServletResponse httpServletResponse) {
            webhook.onUpdate(mapper.readValue(request.getInputStream(), Update.class));
        }
    }
}
