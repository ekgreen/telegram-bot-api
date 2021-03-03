package com.goodboy.telegram.bot.http.api.client.adapter.multipart;

import com.goodboy.telegram.bot.api.platform.upload.FileLoader;

import javax.annotation.Nonnull;

/**
 *
 * @param <V>
 */
public interface MultipartParameter<V> {

    /**
     * @return ключ параметра для тела мультипарт-запрос
     */
    @Nonnull String key();

    /**
     * @return значение параметра для тела мультипарт-запрос
     */
    @Nonnull V value();

    /**
     * Интерфейс для загрузки файлов
     */
    public interface StreamMultipartParameter extends MultipartParameter<FileLoader>{}

    /**
     * Интерфейс для параметров запроса
     */
    public interface SimpleMultipartParameter extends MultipartParameter<String>{}
}
