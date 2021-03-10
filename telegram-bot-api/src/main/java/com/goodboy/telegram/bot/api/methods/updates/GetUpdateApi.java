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

import com.goodboy.telegram.bot.api.meta.ApiQuery;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@TelegramApi
@Data
@Accessors(chain = true)
@ApiQuery(method = TelegramMethodApiDefinition.GET_UPDATES, provides = Updates.class)
public class GetUpdateApi implements Api {

    /**
     * Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of
     * previously received updates. By default, updates starting with the earliest unconfirmed update are returned.
     * An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id.
     * The negative offset can be specified to retrieve updates starting from -offset update from the end of the updates
     * queue. All previous updates will forgotten.
     *
     * @optional
     */
    private Long offset;

    /**
     * Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     *
     * @optional
     */
    private Integer limit;

    /**
     * Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling. Should be positive, short polling
     * should be used for testing purposes only.
     *
     * @optional
     */
    private Long timeout;

    /**
     *  A JSON-serialized list of the update types you want your bot to receive. For example, specify [“message”,
     *  “edited_channel_post”, “callback_query”] to only receive updates of these types. See Update for a complete list
     *  of available update types. Specify an empty list to receive all updates regardless of type (default). If not
     *  specified, the previous setting will be used.
     *
     * Please note that this parameter doesn't affect updates created before the call to the getUpdates, so unwanted
     * updates may be received for a short period of time.
     *
     * @optional
     */
    private List<UpdateType> allowedUpdates;
}
