package com.goodboy.telegram.bot.spring.impl.configuration;

import com.goodboy.telegram.bot.spring.impl.processors.SkipInFavorBPPMethodArgumentResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class TelegramApiSpringCoreConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SkipInFavorBPPMethodArgumentResolver());
    }
}
