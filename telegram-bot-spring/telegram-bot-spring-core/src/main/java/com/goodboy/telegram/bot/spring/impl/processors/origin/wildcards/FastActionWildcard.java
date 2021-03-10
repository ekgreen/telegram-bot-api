package com.goodboy.telegram.bot.spring.impl.processors.origin.wildcards;

import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.spring.api.actions.FastAction;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.processor.barrier.OriginChainWildcard;
import com.goodboy.telegram.bot.spring.api.processor.barrier.Wildcard;
import com.goodboy.telegram.bot.spring.api.processor.barrier.WildcardApiExecutor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Infrastructure
public class FastActionWildcard implements OriginChainWildcard<FastAction<Api>> {
    @Override
    public Class<?> wildcardType() {
        return FastAction.class;
    }

    @Override
    public Wildcard<FastAction<Api>> createWildcard(@NotNull BotData botData, @NotNull Class<?> returnType) {
        return OriginFastAction::new;
    }

    @RequiredArgsConstructor
    private static class OriginFastAction implements FastAction<Api> {

        // origin fast action that returns
        private final FastAction<Api> origin;
        private final WildcardApiExecutor executor;

        @Override
        public Api heavyweight() {
            // invoke origin method
            final Api heavyweight = origin.heavyweight();

            // execute api
            if (heavyweight != null)
                executor.sendApi(heavyweight);

            // return origin
            return heavyweight;
        }
    }
}
