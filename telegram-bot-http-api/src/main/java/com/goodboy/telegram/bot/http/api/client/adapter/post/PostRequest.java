package com.goodboy.telegram.bot.http.api.client.adapter.post;

import com.goodboy.telegram.bot.http.api.client.adapter.UniTypeRequest;

import javax.annotation.Nonnull;

/**
 *
 */
public interface PostRequest extends UniTypeRequest<byte[]> {

    /**
     *
     * @return
     */
    @Nonnull byte[] payload();
}
