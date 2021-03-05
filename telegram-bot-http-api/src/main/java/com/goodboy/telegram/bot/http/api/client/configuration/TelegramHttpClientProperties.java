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


import com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class TelegramHttpClientProperties {

    /**
     * Throw exception on non 200 response code
     */
    private boolean throwExceptionOnNonOkResponse = true;

    /**
     * Desire http method on controversial situations
     */
    private HttpMethod desireHttpMethod = HttpMethod.POST;
}
