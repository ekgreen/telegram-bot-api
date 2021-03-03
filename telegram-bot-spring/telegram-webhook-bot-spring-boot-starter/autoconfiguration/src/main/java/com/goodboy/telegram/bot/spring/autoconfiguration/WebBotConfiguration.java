package com.goodboy.telegram.bot.spring.autoconfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.http.api.client.response.UpdateProvider;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiRuntimeException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class WebBotConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UpdateProvider updateProvider(ObjectMapper mapper, HttpServletRequest request) {
        return () -> {
            try {
                return mapper.readValue(request.getInputStream(), Update.class);
            }catch (Exception e) {
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "fail to read request", e);
            }
        };
    }
}
