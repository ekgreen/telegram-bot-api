package com.goodboy.telegram.bot.spring;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

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
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class TelegramBotServlet extends HttpServlet implements BotWebhookListener {

    private final TelegramEnvironment environment;
    private final Map<String, Object> requestHandlers;
    private final String rootContext;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    public void onWebhookCreation(@NotNull Webhook webhook, @NotNull WebhookBeanDefinition info) {

    }
}
