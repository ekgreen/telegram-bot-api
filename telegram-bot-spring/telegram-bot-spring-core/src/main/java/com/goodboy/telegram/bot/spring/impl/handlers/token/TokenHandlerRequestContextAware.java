package com.goodboy.telegram.bot.spring.impl.handlers.token;

import com.goodboy.telegram.bot.spring.api.handlers.token.Token;
import com.goodboy.telegram.bot.spring.api.handlers.token.TokenNotDefined;
import com.goodboy.telegram.bot.spring.api.handlers.token.TokenSupplier;
import com.goodboy.telegram.bot.spring.api.listeners.OnBotCreationListener;
import com.goodboy.telegram.bot.spring.api.bot.Bot;
import com.goodboy.telegram.bot.spring.api.meta.BotBeanDefinition;
import com.goodboy.telegram.bot.spring.api.meta.InfrastructureBean;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@InfrastructureBean
public class TokenHandlerRequestContextAware extends OncePerRequestFilter implements OnBotCreationListener, BeanPostProcessor, TokenSupplier {

    private final static ThreadLocal<String> local = new ThreadLocal<>();

    private final Map<String, String> tokensByPath = new HashMap<>();
    private final Map<String, String> tokensByName = new HashMap<>();

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest httpServletRequest,
            @NotNull HttpServletResponse httpServletResponse,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String path = httpServletRequest.getPathInfo();

        local.set(getTokenByPath(path));
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        local.set(null);
    }

    @Override
    public void onBotCreation(@NotNull Bot webhook, @NotNull BotBeanDefinition definition) {
        final String botName = definition.getBotName();
        final String path = definition.getPath();

        final String token = definition.getTokenHandler().token(botName);

        tokensByName.put(botName, token);
        tokensByPath.put(path, token);
    }

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(com.goodboy.telegram.bot.spring.api.meta.Bot.class))
                .filter(field -> Token.class.isAssignableFrom(field.getType()))
                .findAny()
                .ifPresent(field -> {
                    try {
                        final com.goodboy.telegram.bot.spring.api.meta.Bot bot = field.getAnnotation(com.goodboy.telegram.bot.spring.api.meta.Bot.class);

                        final String botName = StringUtils.isNotEmpty(bot.name()) ? bot.name() :
                                StringUtils.isNotEmpty(bot.value()) ? bot.value() : null;

                        final Token token;

                        if(botName == null){
                            // request scope token
                            token = () ->{
                                final String tokenValue = getRequestToken();

                                if(tokenValue == null)
                                    throw new TokenNotDefined("token not defined in current thread");

                                return tokenValue;
                            };
                        }else {
                            // singleton
                            token = () -> {
                                final String tokenValue = getTokenByName(botName);

                                if(tokenValue == null)
                                    throw new TokenNotDefined(beanName);

                                return tokenValue;
                            };
                        }

                        field.setAccessible(true);
                        field.set(bean, token);

                    } catch (Exception e) {
                        throw new BeanCreationException("can not create token handler" ,e);
                    }
                });

        return bean;
    }

    public String getRequestToken() {
        return local.get();
    }

    public String getTokenByName(@NotNull String botName) {
        return tokensByName.get(botName);
    }

    public String getTokenByPath(@NotNull String path) {
        return tokensByPath.get(path);
    }
}
