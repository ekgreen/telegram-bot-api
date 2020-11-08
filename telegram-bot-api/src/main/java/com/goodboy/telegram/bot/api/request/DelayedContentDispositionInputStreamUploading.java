package com.goodboy.telegram.bot.api.request;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class DelayedContentDispositionInputStreamUploading implements ContentDispositionUploading<InputStream> {

    private final String content;
    private final Supplier<InputStream> stream;

    @Override
    public Supplier<InputStream> uploading() {
        return stream;
    }

    @Override
    public String disposition() {
        return content;
    }
}
