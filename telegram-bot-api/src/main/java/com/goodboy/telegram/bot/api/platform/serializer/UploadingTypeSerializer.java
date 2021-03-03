package com.goodboy.telegram.bot.api.platform.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.goodboy.telegram.bot.api.platform.upload.Uploading;

import java.io.IOException;

public class UploadingTypeSerializer extends JsonSerializer<Uploading> {

    /**
     * Порядок сериализации следующий:
     *
     *  1) Идентфикатор файла;
     *  2) Ссылка на ресурс;
     *  3) NULL так как для отправки НОВЫХ документов нужно использовать multipart запросы
     *
     */
    public void serialize(Uploading uploading, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String value = null;

        if(uploading != null) {

            if (uploading.fileId() != null) {
                value = uploading.fileId();
            } else if (uploading.httpLink() != null) {
                value = uploading.httpLink();
            }

        }

        jsonGenerator.writeString(value);
    }
}
