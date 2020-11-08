package com.goodboy.telegram.bot.api.request;

import java.util.function.Supplier;

public interface ContentDispositionUploading<T> extends Uploading<Supplier<T>> {

    String disposition();
}
