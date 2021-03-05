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

package com.goodboy.telegram.bot.api.platform.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.goodboy.telegram.bot.api.platform.upload.Uploading;

import java.io.IOException;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public class UploadingTypeSerializer extends JsonSerializer<Uploading> {

    /**
     * Порядок сериализации следующий:
     * <p>
     * 1) Идентфикатор файла;
     * 2) Ссылка на ресурс;
     * 3) NULL так как для отправки НОВЫХ документов нужно использовать multipart запросы
     */
    public void serialize(Uploading uploading, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String value = null;

        if (uploading != null) {

            if (uploading.fileId() != null) {
                value = uploading.fileId();
            } else if (uploading.httpLink() != null) {
                value = uploading.httpLink();
            }

        }

        jsonGenerator.writeString(value);
    }
}
