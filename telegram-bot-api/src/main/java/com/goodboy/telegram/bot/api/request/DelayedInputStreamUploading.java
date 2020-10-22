package com.goodboy.telegram.bot.api.request;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class DelayedInputStreamUploading implements StreamUploading<InputStream> {

    private final String name;
    private final Supplier<InputStream> stream;

    @Override
    public String uploadingName() {
        return name;
    }

    @Override
    public Supplier<InputStream> uploading() {
        return stream;
    }
}
