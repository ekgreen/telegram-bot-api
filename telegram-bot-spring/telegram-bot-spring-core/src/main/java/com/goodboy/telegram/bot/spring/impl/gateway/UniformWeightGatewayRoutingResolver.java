/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.spring.impl.gateway;

import com.goodboy.telegram.bot.spring.api.gateway.GatewayRoutingDefinition;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayRoutingResolver;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayUpdate;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public class UniformWeightGatewayRoutingResolver implements GatewayRoutingResolver {

    // initial weight is always equal to 0
    private final static int INITIAL_WEIGHT = 0;
    // addition weight on each hit on condition
    private final static int GRAM = 1;

    @Override
    public float weight(@NotNull GatewayRoutingDefinition definition, @NotNull GatewayUpdate update) {
        float weight = INITIAL_WEIGHT;

        // calculate additional weight based on message text
        weight = weight + calculateAdditionalWeightByMessageText(definition, update);

        return weight;
    }

    private float calculateAdditionalWeightByMessageText(GatewayRoutingDefinition definition, GatewayUpdate update) {
        float weight = INITIAL_WEIGHT;

        // rout by command (no need now)
        // final String messageTextFromUpdate = update.findMessageTextFromUpdate();

        // by commands
        final String command = update.findMessageCommand();

        final Set<String> commands;

        // calculate weight by commands
        if (command != null)
            // command defined by request
            if ((commands = definition.getCommands()) != null && !commands.isEmpty()) {
                // command gram means that if there are 3 commands declared in hook
                // and hits one of them is less priority than 1 of 1
                final float command_gram = (float) GRAM / commands.size();

                if (commands.contains(command))
                    weight = weight + command_gram;
            } else
                weight = weight + DEFAULT_HIT_SALT; // hook not defined any command = could execute each of them
        else
            // command not defined by request
            if ((commands = definition.getCommands()) == null || commands.isEmpty())
                weight = weight + GRAM; // hook not defined any command = could execute each of them

        return weight;
    }

}
