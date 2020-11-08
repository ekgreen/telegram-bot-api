package com.goodboy.telegram.bot.api.method.webhook;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.api.method.message.SendPhotoApi;
import com.goodboy.telegram.bot.api.method.message.TelegramMessageApi;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

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
    @NotNull TelegramCoreResponse<Boolean> setWebhook(@NotNull SetWebhookApi request);

    /**
     * Single-argument method includes required parameter
     *
     * @param url url for callback
     *
     * @see TelegramWebhookApi#setWebhook(SetWebhookApi)
     */
    default @NotNull TelegramCoreResponse<Boolean> setWebhook(@NotNull String url){
        return setWebhook(new SetWebhookApi()
                .setUrl(url)
        );
    }

    /**
     * Use this method to specify a url and receive incoming updates via an outgoing webhook with specify request data like token
     *
     * @see TelegramWebhookApi#setWebhook(SetWebhookApi)
     */
    default @NotNull TelegramCoreResponse<Boolean> setWebhook(Consumer<Request<SetWebhookApi>> handler){
        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "missed token");
    }

    /**
     * Use this method to remove webhook integration if you decide to switch back to getUpdates
     *
     * @return returns True on success
     */
    @NotNull TelegramCoreResponse<Boolean> deleteWebhook();

    /**
     * Use this method to remove webhook integration if you decide to switch back to getUpdates with specify request data like token
     *
     * @see TelegramWebhookApi#deleteWebhook()
     */
    default @NotNull TelegramCoreResponse<Boolean> deleteWebhook(Consumer<Request<DeleteWebhookApi>> handler){
        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "missed token");
    }

    /**
     * Use this method to get current webhook status. Requires no parameters.
     * If the bot is using getUpdates, will return an object with the url field empty
     *
     * @return on success, returns a WebhookInfo object
     */
    @NotNull TelegramCoreResponse<WebhookInfo> getWebhookInfo();

    /**
     * Use this method to get current webhook status. Requires no parameters with specify request data like token
     *
     * @see TelegramWebhookApi#getWebhookInfo()
     */
    default @NotNull TelegramCoreResponse<WebhookInfo> getWebhookInfo(Consumer<Request<GetWebhookApi>> handler){
        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "missed token");
    }

}
