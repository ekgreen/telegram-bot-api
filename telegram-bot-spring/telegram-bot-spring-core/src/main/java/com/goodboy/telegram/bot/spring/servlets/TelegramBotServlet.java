package com.goodboy.telegram.bot.spring.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.spring.meta.Webhook;
import com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition;
import com.goodboy.telegram.bot.spring.meta.BotWebhookListener;
import com.goodboy.telegram.bot.spring.toolbox.Servlet;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition.RegistryBot.NON;
import static com.goodboy.telegram.bot.spring.meta.WebhookBeanDefinition.RegistryStatus.REGISTERED;

/**
 *
 */
@Slf4j
@Servlet
@RequiredArgsConstructor
public class TelegramBotServlet extends HttpServlet implements BotWebhookListener {

    private final ObjectMapper mapper;
    private final Map<String, Object> requestHandlers = new HashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        // simple realisation of http servlet without any tinsel

        // 1) get path from http request
        final String path = req.getPathInfo();

        // 2) search handler by path
        if (requestHandlers.containsKey(path))
            // 3) path resolver by handler type
            callOnUntyped(requestHandlers.get(path)).accept(req, resp);

        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "not found any handler for url [" + path + "]");
    }

    @Override
    public void onWebhookCreation(final @NotNull Webhook webhook, @NotNull WebhookBeanDefinition definition) {
        // 1) Define path. wildcards/expressions not supported
        final String path = definition.getPath();

        // 2) Validate path on uniqueness
        if (requestHandlers.containsKey(path)) {
            log.error("Attempt to registry two (or more) objects on single unique path { path = {}, new_handler = {}, exist_handler = {} }", path, webhook.getClass().getSimpleName(), requestHandlers.get(path).getClass().getSimpleName());
            throw new BeanCreationException("uniqueness violation. webhook registry in servlet container exception");
        }

        // 3) Create proxy for self registry webhooks
        @NotNull Webhook candidate = webhook;

        if (definition.getSelfRegistry() != NON) {

            candidate = update -> {
                var status = definition.getRegistryStatusPules().get();

                if (status == REGISTERED)
                    webhook.onUpdate(update);

                // probably hack case, when request coming not form registry server
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "webhook not initialised yet [" + definition.getBotName() + "]");
            };
        }

        requestHandlers.put(path, candidate);
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> callOnUntyped(@Nonnull Object handler) {
        if (handler instanceof Webhook)
            return new WebhookBiConsumer((Webhook) handler, mapper);

        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "unknown handler [" + handler.getClass().getSimpleName() + "] type");
    }

    @RequiredArgsConstructor
    private static class WebhookBiConsumer implements BiConsumer<HttpServletRequest, HttpServletResponse> {

        private final Webhook webhook;
        private final ObjectMapper mapper;

        @SneakyThrows
        public void accept(HttpServletRequest request, HttpServletResponse httpServletResponse) {
            webhook.onUpdate(mapper.readValue(request.getInputStream(), Update.class));
        }
    }
}
