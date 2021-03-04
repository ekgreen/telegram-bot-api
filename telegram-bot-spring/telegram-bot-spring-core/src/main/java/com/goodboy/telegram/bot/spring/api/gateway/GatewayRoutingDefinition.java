package com.goodboy.telegram.bot.spring.api.gateway;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * Defines routing constants which should be used for routing calclatiuon
 */
@Data
@Accessors(chain = true)
public class GatewayRoutingDefinition {

    // commands available for current hook
    private Set<String> commands;
}