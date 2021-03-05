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

package com.goodboy.telegram.bot.api.response;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Supplier;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class TelegramCoreResponse<T> {

    /**
     * Success sign
     */
    private Boolean ok;

    /**
     * Error code
     *
     * @optional
     */
    private @Optional
    Integer errorCode;

    /**
     * Description of request action
     *
     * @optional
     */
    private @Optional
    String description;

    /**
     * Additional response parameters
     *
     * @optional
     */
    private @Optional
    ResponseParameters parameters;

    /**
     * Body
     *
     * @optional
     */
    private @Optional
    T result;

    /**
     * Use this method when you want to change TelegramCoreResponse on Optional
     *
     * @return optional
     */
    public java.util.Optional<T> get() {
        return java.util.Optional.ofNullable(isOk() ? result : null);
    }

    /**
     * Use this method when you want to change TelegramCoreResponse as Optional sequence
     *
     * @return optional
     */
    public T orElseGet(Supplier<T> handler) {
        return get().orElseGet(handler);
    }

    /**
     * Use this method when you want to change TelegramCoreResponse on Optional and throw
     * exception on false response
     *
     * @param exception runtime exception generator
     * @return optional
     */
    public java.util.Optional<T> orThrow(Supplier<? extends RuntimeException> exception) {
        if (!isOk())
            throw exception.get();

        return java.util.Optional.ofNullable(result);
    }

    /**
     * Wrapper around Boolean
     *
     * @return prime
     */
    public boolean isOk() {
        return ok != null && ok;
    }
}
