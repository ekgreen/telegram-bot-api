package com.goodboy.telegram.bot.spring.impl.gateway;

import com.goodboy.telegram.bot.spring.api.gateway.GatewayRoutingDefinition;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayRoutingResolver;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayUpdate;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

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
