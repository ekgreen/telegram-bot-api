package com.goodboy.telegram.bot.api.request;

import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class DelayedByteArrayUploading implements StreamUploading<byte[]> {

    private final String name;
    private final Supplier<byte[]> stream;

    @Override
    public String uploadingName() {
        return name;
    }

    @Override
    public Supplier<byte[]> uploading() {
        return stream;
    }
}
