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

import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import org.jetbrains.annotations.NotNull;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public interface TelegramWebhookApi {

    /**
     * Use this method to specify a url and receive incoming updates via an outgoing webhook.
     * Whenever there is an update for the bot, we will send an HTTPS POST request to the specified url,
     * containing a JSON-serialized Update. In case of an unsuccessful request, we will give up after
     * a reasonable amount of attempts
     * <p>
     * If you'd like to make sure that the Webhook request comes from Telegram, we recommend using a secret path
     * in the URL, e.g. https://www.example.com/<token>. Since nobody else knows your bot's token,
     * you can be pretty sure it's us.
     *
     * @return returns True on success
     * @see <a href="https://core.telegram.org/bots/api#setwebhook">setWebhook</a>
     */
    @NotNull TelegramCoreResponse<Boolean> setWebhook(@NotNull SetWebhookApi request);

    /**
     * Single-argument method includes required parameter
     *
     * @param url url for callback
     * @see TelegramWebhookApi#setWebhook(SetWebhookApi)
     */
    default @NotNull TelegramCoreResponse<Boolean> setWebhook(@NotNull String url) {
        return setWebhook(new SetWebhookApi()
                .setUrl(url)
        );
    }

    /**
     * Use this method to remove webhook integration if you decide to switch back to getUpdates
     *
     * @return returns True on success
     */
    @NotNull TelegramCoreResponse<Boolean> deleteWebhook();

    /**
     * Use this method to get current webhook status. Requires no parameters.
     * If the bot is using getUpdates, will return an object with the url field empty
     *
     * @return on success, returns a WebhookInfo object
     */
    @NotNull TelegramCoreResponse<WebhookInfo> getWebhookInfo();

}
