package com.goodboy.telegram.bot.spring.api.gateway;

import javax.annotation.Nonnull;

public interface GatewayRoutingResolver {

    // salt for default hits - add some minor value to separate missed from default hit
    public final static float DEFAULT_HIT_SALT = 0.0000000000001f;

    float weight(@Nonnull GatewayRoutingDefinition definition, @Nonnull GatewayUpdate update);
}
