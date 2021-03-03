package com.goodboy.telegram.bot.http.api.client.token;

import com.goodboy.telegram.bot.http.api.client.context.TelegramApiContextResolver;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Резолвер основанный на получении данных из контекста, то есть эти данные сначала должны появиться в контексте
 * из другого места, например из другого резолвера который завязан на иной источник данных, а только затем мы сможем
 * их получить в данном экземпляре
 *
 * Обозначения:
 * TR - {@link TelegramApiTokenResolver}
 * C - {@link UpdateContext}
 *
 * Схема:
 * TR -> C -> TR {@link ContextBasedTokenResolver}
 *
 */
@RequiredArgsConstructor
public class ContextBasedTokenResolver implements TelegramApiTokenResolver {

    private final TelegramApiContextResolver resolver;

    public String get() {
        final var context = resolver.read();

        return Optional.ofNullable(context).map(UpdateContext::getBotToken).orElse(null);
    }

}
