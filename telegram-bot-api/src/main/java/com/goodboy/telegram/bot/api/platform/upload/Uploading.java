package com.goodboy.telegram.bot.api.platform.upload;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.platform.serializer.UploadingTypeSerializer;

@JsonSerialize(using = UploadingTypeSerializer.class)
public interface Uploading{

    /**
     * Pass a file_id as String to send an audio file that exists on the Telegram servers (recommended)
     * @return file_id
     */
    @Optional
    String fileId();

    /**
     * Pass an HTTP URL as a String for Telegram to get an audio file from the Internet
     * @return link
     */
    default  @Optional String httpLink(){
        return null;
    }

    /**
     * Upload a new one using multipart/form-data
     * @return file
     */
    default @Optional FileLoader uploadNewFile(){
        return null;
    }
}
