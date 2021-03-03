package com.goodboy.telegram.bot.spring.api.sender;

import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;

import javax.annotation.Nonnull;

public interface ApiMethodExecutor<T extends Api> {

    void handle(@Nonnull UpdateContext context, @Nonnull T api);

    @Nonnull Class<T> type();
}
