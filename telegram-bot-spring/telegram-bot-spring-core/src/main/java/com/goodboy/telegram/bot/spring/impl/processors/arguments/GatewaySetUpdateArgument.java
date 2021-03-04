package com.goodboy.telegram.bot.spring.impl.processors.arguments;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.http.api.client.response.UpdateProvider;
import com.goodboy.telegram.bot.spring.api.data.BotData;
import com.goodboy.telegram.bot.spring.api.events.OnBotRegistry;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayFilter;
import com.goodboy.telegram.bot.spring.api.gateway.GatewayFilterChain;
import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// todo split provider and filter
@Infrastructure
public class GatewaySetUpdateArgument implements GatewayFilter, UpdateProvider, OnBotRegistry {

    // default position = no position
    private final static int DEFAULT_UPDATE_POSITION = -1;
    // cache for request scopes updates
    private final static ThreadLocal<Update> CACHE = new ThreadLocal<>();
    // relationship btw method and update argument position
    private final Map<Method, Integer> relationship = new HashMap<>(); // todo поменять ключ

    @Override
    public Object invoke(Object proxy, Method method, Object[] args, Update update, GatewayFilterChain chain) {
        try {
            // add in cache
            CACHE.set(update);

            // add in argument
            Integer position;

            if ((position = relationship.get(method)) != null)
                args[position] = update;

            return chain.invoke(proxy, method, args, update);
        }finally {
            CACHE.remove();
        }
    }


    @Override
    public Update getUpdate() {
        return CACHE.get();
    }

    @Override
    public void onRegistry(@NotNull BotData data) {
        final Class<?> originBotType = data.getOriginBotType();

        final Method[] declaredMethods = originBotType.getDeclaredMethods();

        for (Method declaredMethod : declaredMethods) {
            final Class<?>[] parameterTypes = declaredMethod.getParameterTypes();

            final int position = findUpdatePosition(parameterTypes);

            if (position != DEFAULT_UPDATE_POSITION)
                relationship.put(declaredMethod, position);
        }
    }

    private int findUpdatePosition(Class<?>[] parameterTypes) {

        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];

            if (Update.class.isAssignableFrom(parameterType))
                return i;
        }

        return DEFAULT_UPDATE_POSITION;
    }
}
