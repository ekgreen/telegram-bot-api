package com.goodboy.telegram.bot.http.api.client.adapter.multipart;

import com.goodboy.telegram.bot.http.api.client.adapter.UniTypeRequest;

import javax.annotation.Nonnull;

/**
 *
 */
public interface MultipartRequest extends UniTypeRequest<Iterable<MultipartParameter<?>>> {

    @Nonnull Iterable<MultipartParameter<?>> payload();
}
