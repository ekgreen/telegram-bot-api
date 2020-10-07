package com.goodboy.telegram.bot.core.method.message;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.method.message.SendMessageApi;
import com.goodboy.telegram.bot.api.method.message.TelegramMessageApi;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class ProxyRemoteTelegramMessageApiCall implements TelegramMessageApi {

    @Nonnull
    @Override
    public TelegramCoreResponse<Message> sendMessage(@Nonnull SendMessageApi request) throws IOException, InterruptedException {


        return null;
    }
}
