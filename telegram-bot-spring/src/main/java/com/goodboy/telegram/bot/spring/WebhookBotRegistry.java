package com.goodboy.telegram.bot.spring;

import com.goodboy.telegram.bot.api.User;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.api.method.me.TelegramMeApi;
import com.goodboy.telegram.bot.api.method.webhook.SetWebhookApi;
import com.goodboy.telegram.bot.api.method.webhook.TelegramWebhookApi;
import com.goodboy.telegram.bot.api.method.webhook.WebhookInfo;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

import java.util.function.Supplier;

import static com.goodboy.telegram.bot.spring.WebhookBeanDefinition.RegistryBot.NON;
import static com.goodboy.telegram.bot.spring.WebhookBeanDefinition.RegistryBot.REWRITE;

@InfrastructureBean
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WebhookBotRegistry implements BotWebhookListener {

    private final UrlResolver urlResolver;
    private final TelegramWebhookApi webhookApi;

    @Override
    public void onWebhookCreation(@NotNull Webhook webhook, @NotNull WebhookBeanDefinition definition) {
        if (definition.getSelfRegistry() != NON) {
            registry(definition);
        }
    }

    private void registry(@NotNull WebhookBeanDefinition definition) {
        // 1) определяем зарегестрировал ли webhook
        @Nonnull TelegramCoreResponse<WebhookInfo> response = webhookApi.getWebhookInfo(definition.getTokenHandler().token(definition.getBotName()));

        final Supplier<? extends RuntimeException> generator = () -> new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_RESPONSE_ERROR);

        WebhookInfo webhookInfo = response
                .orThrow(generator)
                .orElseThrow(generator);

        SetWebhookApi api;

        @Nullable String url = webhookInfo.getUrl();

        if ( (StringUtils.isNotEmpty(url) && definition.getSelfRegistry() == REWRITE) || (StringUtils.isEmpty(url))) {
            api = new SetWebhookApi()
                    .setUrl(urlResolver.getContextUrl() + definition.getPath())
                    .setCertificate(definition.getCertificateProvider().certificate(definition.getBotName()))
                    .setMaxConnections(definition.getMaxConnections());

            webhookApi.setWebhook(api);
        }


    }

}
