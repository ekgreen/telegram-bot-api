package com.goodboy.telegram.bot.http.api.client.configuration;

import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 *
 */
public interface TelegramApiConfiguration {

    /**
     * Интерфейс по получению актуального protocol://host:port - базовая имплементация, хардкод значения
     *
     * @return telegram api url
     */
    @Nonnull default String getTelegramApiUrl(){
        return TelegramMethodApiDefinition.DEFAULT_HOST;
    }

    /**
     * Выбрасывать исключение при получении кода ответа отличного от 2ХХ
     *
     * @return                признак, если задан
     */
    Optional<Boolean> getThrowExceptionOnNon2XXHttpCode();

    /**
     * Определяем правило описанное в {@link this#getThrowExceptionOnNon2XXHttpCode()}
     *
     * @param code актуальный код операции
     * @return признак
     */
    default boolean throwExceptionOnNon2XXHttpCode(int code) {
        return getThrowExceptionOnNon2XXHttpCode().orElseThrow(NullPointerException::new) && code / 100 != 2;
    }

    /**
     * Желаемый способ запросов к Telegram API
     *
     * @return                метод HTTP
     */
    Optional<HttpMethod> desireHttpMethod();
}
            