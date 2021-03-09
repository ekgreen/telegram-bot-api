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

package com.goodboy.telegram.bot.api.methods.updates;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.methods.ApiRequest;
import com.goodboy.telegram.bot.api.methods.webhook.SetWebhookApi;
import com.goodboy.telegram.bot.api.methods.webhook.WebhookInfo;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public interface TelegramGetUpdatesApi {

    /**
     * Use this method to receive incoming updates using long polling.
     *
     * @return an Array of Update objects is returned
     */
    @Nonnull TelegramCoreResponse<Updates> getUpdates();

    /**
     * Use this method to receive incoming updates using long polling.
     *
     * offset	        Integer	        Optional	Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id. The negative offset can be specified to retrieve updates starting from -offset update from the end of the updates queue. All previous updates will forgotten.
     * limit	        Integer	        Optional	Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     * timeout	        Integer	        Optional	Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive, short polling should be used for testing purposes only.
     * allowed_updates	Array of String	Optional	A JSON-serialized list of the update types you want your bot to receive. For example, specify [“message”, “edited_channel_post”, “callback_query”] to only receive updates of these types. See Update for a complete list of available update types. Specify an empty list to receive all updates regardless of type (default). If not specified, the previous setting will be used.
     * Please note that this parameter doesn't affect updates created before the call to the getUpdates, so unwanted updates may be received for a short period of time.
     *
     * @return an Array of Update objects is returned
     */
    @Nonnull TelegramCoreResponse<Updates> getUpdates(@Nonnull ApiRequest<GetUpdateApi> request);
}
