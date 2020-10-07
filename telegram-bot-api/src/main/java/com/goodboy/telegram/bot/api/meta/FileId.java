package com.goodboy.telegram.bot.api.meta;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.goodboy.telegram.bot.api.TelegramApiDtoMetadataDescription;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@JacksonAnnotationsInside
@JsonProperty(TelegramApiDtoMetadataDescription.FILE_ID_DESCRIPTOR)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FileId {}
