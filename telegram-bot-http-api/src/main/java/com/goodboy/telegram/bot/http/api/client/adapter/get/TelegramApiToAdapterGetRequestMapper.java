package com.goodboy.telegram.bot.http.api.client.adapter.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.goodboy.telegram.bot.api.meta.Ignore;
import com.goodboy.telegram.bot.api.meta.Json;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.adapter.TelegramApiToAdapterRequestMapper;
import com.goodboy.telegram.bot.api.platform.upload.Uploading;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod.GET;
import static java.lang.reflect.Modifier.isStatic;

@RequiredArgsConstructor
public class TelegramApiToAdapterGetRequestMapper implements TelegramApiToAdapterRequestMapper<Iterable<QueryAttribute>> {

    private final ObjectMapper mapper;

    @SneakyThrows
    public Iterable<QueryAttribute> transform(@Nullable Object o) {
        final List<QueryAttribute> attributes = new ArrayList<>();

        if (o != null)
            enrichAttributeList(o, attributes);

        return attributes;
    }

    @SneakyThrows
    private void enrichAttributeList(@Nonnull Object o, @Nonnull List<QueryAttribute> attributes) {
        final Class<?> type = o.getClass();

        final TelegramApi telegramApi = type.getAnnotation(TelegramApi.class);

        if(telegramApi == null)
            throw new IllegalArgumentException("class not annotated by @TelegramApi = " + type);

        final PropertyNamingStrategyBase naming = (PropertyNamingStrategyBase) telegramApi.annotationType()
                .getAnnotation(JsonNaming.class)
                .value()
                .getConstructor()
                .newInstance();

        Arrays.stream(type.getDeclaredFields())
                .filter(field -> !isStatic(field.getModifiers()))
                .filter(field -> !field.isAnnotationPresent(Ignore.class))
                .map(field -> {
                    final String name = naming.translate(field.getName());
                    final String value;

                    final Class<?> fieldType = field.getType();

                    Optional<Object> fieldValue = getFieldValue(o, field);

                    if (fieldType.isAnnotationPresent(Json.class))
                        value = fieldValue.map(this::writeValueAsJson).orElse(null);
                    else
                        value = fieldValue.map(this::writeValueAsString).orElse(null);

                    return new InnerQueryAttribute(name, value);
                })
                .collect(Collectors.toCollection(() -> attributes));
    }

    @Data
    @Accessors(chain = true, fluent = true)
    private static class InnerQueryAttribute implements QueryAttribute{
        private final String key;
        private final String value;
    }

    @SneakyThrows
    private Optional<Object> getFieldValue(@Nonnull Object object, @Nonnull Field field) {
        field.setAccessible(true);
        return Optional.ofNullable(field.get(object));
    }

    @SneakyThrows
    private String writeValueAsJson(@Nonnull Object object) {
        return mapper.writeValueAsString(object);
    }

    @SneakyThrows
    private String writeValueAsString(@Nonnull Object object) {
        final String value;

        final Class<?> type = object.getClass();

        if(Uploading.class.isAssignableFrom(type)){
            final Uploading uploading = (Uploading) object;

            if (uploading.fileId() != null) {
                value = uploading.fileId();
            } else if (uploading.httpLink() != null) {
                value = uploading.httpLink();
            } else
                value = null;

        }else {
            value = object.toString();
        }

        return value;
    }

    @Override
    public Request.HttpMethod method() {
        return GET;
    }
}
