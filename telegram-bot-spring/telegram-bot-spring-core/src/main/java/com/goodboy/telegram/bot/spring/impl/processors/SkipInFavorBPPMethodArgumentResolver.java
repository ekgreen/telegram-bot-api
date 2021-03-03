package com.goodboy.telegram.bot.spring.impl.processors;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.http.api.client.context.UpdateContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SkipInFavorBPPMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final Class<?> parameterType = parameter.getParameterType();

        return UpdateContext.class.isAssignableFrom(parameterType) || Update.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        /* just skip set of this parameter */
        return null;
    }
}
