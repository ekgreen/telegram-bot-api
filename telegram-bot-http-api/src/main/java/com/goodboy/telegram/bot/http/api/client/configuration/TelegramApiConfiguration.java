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

package com.goodboy.telegram.bot.http.api.client.configuration;

import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
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
            