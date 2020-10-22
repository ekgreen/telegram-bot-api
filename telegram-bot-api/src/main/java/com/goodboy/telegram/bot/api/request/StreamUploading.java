package com.goodboy.telegram.bot.api.request;

import java.util.function.Supplier;

public interface StreamUploading<T> extends Uploading<Supplier<T>> {

    String uploadingName();
}
