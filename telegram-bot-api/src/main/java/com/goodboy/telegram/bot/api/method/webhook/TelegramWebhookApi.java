package com.goodboy.telegram.bot.api.method.webhook;

import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

import javax.annotation.Nonnull;

public interface TelegramWebhookApi {

    /**
     * Use this method to specify a url and receive incoming updates via an outgoing webhook.
     * Whenever there is an update for the bot, we will send an HTTPS POST request to the specified url,
     * containing a JSON-serialized Update. In case of an unsuccessful request, we will give up after
     * a reasonable amount of attempts
     *
     * If you'd like to make sure that the Webhook request comes from Telegram, we recommend using a secret path
     * in the URL, e.g. https://www.example.com/<token>. Since nobody else knows your bot's token,
     * you can be pretty sure it's us.
     *
     * @see <a href="https://core.telegram.org/bots/api#setwebhook">setWebhook</a>
     * @return returns True on success
     */
    @Nonnull TelegramCoreResponse<Boolean> setWebhook(@Nonnull SetWebhookApi request);

    /**
     * Single-argument method includes required parameter
     *
     * @param url url for callback
     *
     * @see TelegramWebhookApi#setWebhook(SetWebhookApi)
     */
    default @Nonnull TelegramCoreResponse<Boolean> setWebhook(@Nonnull String url){
        return setWebhook(new SetWebhookApi()
                .setUrl(url)
        );
    }

    /**
     * Use this method to remove webhook integration if you decide to switch back to getUpdates
     *
     * @return returns True on success
     */
    @Nonnull TelegramCoreResponse<Boolean> deleteWebhook();


    /**
     * Use this method to get current webhook status. Requires no parameters.
     * If the bot is using getUpdates, will return an object with the url field empty
     *
     * @return on success, returns a WebhookInfo object
     */
    @Nonnull TelegramCoreResponse<WebhookInfo> getWebhookInfo();

}
