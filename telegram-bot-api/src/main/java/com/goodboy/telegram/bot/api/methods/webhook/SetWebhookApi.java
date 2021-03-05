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

import com.goodboy.telegram.bot.api.meta.Multipart;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.platform.upload.Uploading;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Data
@Multipart
@TelegramApi
@Accessors(chain = true)
public class SetWebhookApi implements Api {

    /**
     * HTTPS url to send updates to. Use an empty string to remove webhook integration
     */
    private String url;

    /**
     * Upload your public key certificate so that the root certificate in use can be checked
     *
     * @optional
     */
    private @Optional Uploading certificate;

    /**
     * Maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100.
     * Defaults to 40. Use lower values to limit the load on your bot's server, and higher values to increase
     * your bot's throughput.
     *
     * @optional
     */
    private @Optional Integer maxConnections;

    /**
     * A JSON-serialized list of the update types you want your bot to receive. For example, specify [“message”,
     * “edited_channel_post”, “callback_query”] to only receive updates of these types. See Update for
     * a complete list of available update types. Specify an empty list to receive all updates regardless
     * of type (default). If not specified, the previous setting will be used.
     *
     * Please note that this parameter doesn't affect updates created before the call to the setWebhook,
     * so unwanted updates may be received for a short period of time.
     *
     * @optional
     */
    private @Optional List<String> allowedUpdates;
}
