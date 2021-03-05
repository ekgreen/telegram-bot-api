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

package com.goodboy.telegram.bot.http.api.client.request;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class CallMethodImpl implements Request.CallMethod {

    private final String name;
    private final RequestType requestType;
    private final Class<?> returnType;
    private Request.HttpMethod httpMethod;

    public @NotNull String name() {
        return name;
    }

    public @NotNull RequestType type() {
        return requestType;
    }

    public @NotNull Class<?> desireReturnType() {
        return returnType;
    }

    public Optional<Request.HttpMethod> desireHttpMethod() {
        return Optional.ofNullable(httpMethod);
    }
}
