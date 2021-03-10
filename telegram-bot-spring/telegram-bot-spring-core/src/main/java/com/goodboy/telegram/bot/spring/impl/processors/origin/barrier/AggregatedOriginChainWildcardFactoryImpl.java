package com.goodboy.telegram.bot.spring.impl.processors.origin.barrier;

import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.processor.barrier.OriginChainWildcard;
import com.goodboy.telegram.bot.spring.api.processor.barrier.OriginChainWildcardFactory;
import com.goodboy.telegram.bot.spring.api.processor.barrier.Wildcard;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Infrastructure
public class AggregatedOriginChainWildcardFactoryImpl implements OriginChainWildcardFactory {

    private final Map<Class<?>, OriginChainWildcard<?>> wildcards;

    public AggregatedOriginChainWildcardFactoryImpl(@Nullable List<OriginChainWildcard<?>> wildcards) {
        this.wildcards = wildcards != null ?
                ImmutableMap.copyOf(
                        wildcards.stream().collect(Collectors.toMap(
                                OriginChainWildcard::wildcardType,
                                w -> w
                        ))
                )
                :
                ImmutableMap.of();
    }

    @Override
    public boolean isWildcardAvailable(@NotNull Class<?> returnType) {
        return getKey(returnType) != null;
    }

    @Override
    public <T> Wildcard<T> createWildcard(@NotNull BotData botData, @NotNull Class<T> returnType) {
        final Class<?> key = getKey(returnType);

        if(key == null)
            throw new IllegalArgumentException("no wildcards available for type = " + returnType);

        return (Wildcard<T>) wildcards.get(key).createWildcard(botData, returnType);
    }

    private @Nullable Class<?> getKey(@NotNull Class<?> returnType) {
        if(wildcards.containsKey(returnType))
            return returnType;

        return wildcards.keySet().stream().filter(supperType -> supperType.isAssignableFrom(returnType)).findAny().orElse(null);
    }
}