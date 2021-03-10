package com.goodboy.telegram.bot.spring.impl.processors.origin.barrier;

import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import com.goodboy.telegram.bot.spring.api.processor.barrier.OriginChainBarrier;
import com.goodboy.telegram.bot.spring.api.processor.barrier.OriginChainWildcard;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Infrastructure
public class WildcardExcludedOriginChainBarrier implements OriginChainBarrier {

    private final Set<Class<?>> exclusions;

    public WildcardExcludedOriginChainBarrier(@Nullable List<OriginChainWildcard<?>> exclusions) {
        this.exclusions = exclusions != null ?
                ImmutableSet.copyOf(exclusions.stream().map(OriginChainWildcard::wildcardType).collect(Collectors.toSet()))
                :
                ImmutableSet.of();
    }

    @Override
    public boolean isNotUnderArmor(@NotNull Class<?> returnType) {
        return exclusions.contains(returnType) || exclusions.stream().anyMatch(supperType -> supperType.isAssignableFrom(returnType));
    }

}
