/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.api.platform.entry;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.platform.serializer.UploadingTypeSerializer;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@JsonSerialize(using = UploadingTypeSerializer.class)
public interface Uploading {

    /**
     * Pass a file_id as String to send an audio file that exists on the Telegram servers (recommended)
     *
     * @return file_id
     */
    @Optional
    String fileId();

    /**
     * Pass an HTTP URL as a String for Telegram to get an audio file from the Internet
     *
     * @return link
     */
    default @Optional
    String httpLink() {
        return null;
    }

    /**
     * Upload a new one using multipart/form-data
     *
     * @return file
     */
    default @Optional
    FileLoader uploadNewFile() {
        return null;
    }
}
