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

package com.goodboy.telegram.bot.api.methods.webhook;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Contains information about the current status of a webhook
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
public class WebhookInfo {

    /**
     * Webhook URL, may be empty if webhook is not set up
     */
    private String url;

    /**
     * True, if a custom certificate was provided for webhook certificate checks
     */
    private Boolean hasCustomCertificate;

    /**
     * Number of updates awaiting delivery
     */
    private Integer pendingUpdateCount;

    /**
     * Unix time for the most recent error that happened when trying to deliver an update via webhook
     *
     * @optional
     */
    private @Optional
    Integer lastErrorDate;

    /**
     * Error message in human-readable format for the most recent error that happened when trying
     * to deliver an update via webhook
     *
     * @optional
     */
    private @Optional
    String lastErrorMessage;

    /**
     * Maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery
     *
     * @optional
     */
    private @Optional
    Integer maxConnections;

    /**
     * A list of update types the bot is subscribed to. Defaults to all update types
     *
     * @optional
     */
    private @Optional
    List<String> allowedUpdates;
}
