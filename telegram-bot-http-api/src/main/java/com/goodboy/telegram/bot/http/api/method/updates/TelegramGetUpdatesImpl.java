package com.goodboy.telegram.bot.http.api.method.updates;

import com.goodboy.telegram.bot.api.methods.ApiRequest;
import com.goodboy.telegram.bot.api.methods.TelegramMethodApiDefinition;
import com.goodboy.telegram.bot.api.methods.updates.GetUpdateApi;
import com.goodboy.telegram.bot.api.methods.updates.TelegramGetUpdatesApi;
import com.goodboy.telegram.bot.api.methods.updates.Updates;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class TelegramGetUpdatesImpl implements TelegramGetUpdatesApi {

    private final TelegramHttpClient client;

    public @NotNull TelegramCoreResponse<Updates> getUpdates() {
        return getUpdates(ApiRequest.empty());
    }

    public @NotNull TelegramCoreResponse<Updates> getUpdates(@Nonnull ApiRequest<GetUpdateApi> request) {
        return client.send(new MethodRequest<>(
                        new CallMethodImpl(TelegramMethodApiDefinition.GET_UPDATES, RequestType.COMMAND, Updates.class)
                )
                        .setPayload(request.getApi())
                        .setToken(request.getToken())
        );
    }
}
