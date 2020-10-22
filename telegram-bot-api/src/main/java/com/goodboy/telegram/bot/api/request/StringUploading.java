package com.goodboy.telegram.bot.api.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StringUploading implements Uploading<String> {

    private final String fileIdOrUrl;

    @Override
    public String uploading() {
        return fileIdOrUrl;
    }
}
