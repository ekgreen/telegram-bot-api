package com.goodboy.telegram.bot.http.api.client.adapter;

import com.goodboy.telegram.bot.http.api.client.response.TelegramHttpResponse;

import java.util.function.Supplier;

/**
 *
 */
public interface Callback extends Supplier<TelegramHttpResponse> {}
