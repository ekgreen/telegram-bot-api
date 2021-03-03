package com.goodboy.telegram.bot.http.api.client.adapter.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.adapter.TelegramApiToAdapterRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import static com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod.POST;

@RequiredArgsConstructor
public class TelegramApiToAdapterPostRequestMapper implements TelegramApiToAdapterRequestMapper<byte[]> {

    private final ObjectMapper mapper;

    @SneakyThrows
    public byte[] transform(Object o) {
        return mapper.writeValueAsBytes(o);
    }

    @Override
    public Request.HttpMethod method() {
        return POST;
    }
}
