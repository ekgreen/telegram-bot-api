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

package com.goodboy.telegram.bot.http.api.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramApiExceptionDefinitions {

    /**
     * Any technical exception. Core literal (prefix) for other
     * technical exceptions
     */
    public static final String TECHNICAL_EXCEPTION = "TECHNICAL_EXCEPTION";

    /**
     * Any gateway exception. Core literal (prefix) for other
     * gateway exceptions
     */
    public static final String GATEWAY_EXCEPTION = "GATEWAY_EXCEPTION";

    /**
     * Any entity validation exception. Core literal (prefix) for other
     * validation exceptions
     */
    public static final String VALIDATION_EXCEPTION = "VALIDATION_EXCEPTION";

    /**
     * Http response returned with status code different from 200
     */
    public static final String NOT_200_HTTP_STATUS_CODE = "VALIDATION_EXCEPTION:HTTP:NOT_200";

    /**
     * Any http response exception. Core literal (prefix) for other
     * http integration exceptions
     */
    public static final String HTTP_RESPONSE_ERROR = "HTTP_RESPONSE_ERROR";

    /**
     * Any http request-creation exception. Core literal (prefix) for other
     * http integration exceptions
     */
    public static final String HTTP_REQUEST_ERROR = "HTTP_REQUEST_ERROR";
}
