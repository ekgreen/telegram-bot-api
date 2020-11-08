package com.goodboy.telegram.bot.api.request;

import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class DelayedContentDispositionByteArrayUploading implements ContentDispositionUploading<byte[]> {

    private final String content;
    private final Supplier<byte[]> stream;

    @Override
    public Supplier<byte[]> uploading() {
        return stream;
    }

    @Override
    public String disposition() {
        return content;
    }
}