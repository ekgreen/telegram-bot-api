package com.goodboy.telegram.bot.http.api.client.adapter.multipart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.goodboy.telegram.bot.api.meta.Ignore;
import com.goodboy.telegram.bot.api.meta.Json;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.platform.upload.FileLoader;
import com.goodboy.telegram.bot.api.platform.upload.Uploading;
import com.goodboy.telegram.bot.http.api.client.adapter.TelegramApiToAdapterRequestMapper;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartParameter.SimpleMultipartParameter;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartParameter.StreamMultipartParameter;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.goodboy.telegram.bot.http.api.client.request.Request.HttpMethod.MULTIPART;
import static java.lang.reflect.Modifier.isStatic;

@RequiredArgsConstructor
public class TelegramApiToAdapterMultipartRequestMapper implements TelegramApiToAdapterRequestMapper<Iterable<MultipartParameter<?>>> {

    private final ObjectMapper mapper;

    @SneakyThrows
    public Iterable<MultipartParameter<?>> transform(@Nullable Object o) {
        final List<MultipartParameter<?>> parameters = new ArrayList<>();

        if (o != null)
            enrichParameterList(o, parameters);

        return parameters;
    }

    @SneakyThrows
    private void enrichParameterList(@Nonnull Object o, @Nonnull List<MultipartParameter<?>> parameters) {
        final Class<?> type = o.getClass();

        final TelegramApi telegramApi = type.getAnnotation(TelegramApi.class);

        if (telegramApi == null)
            throw new IllegalArgumentException("class not annotated by @TelegramApi = " + type);

        final PropertyNamingStrategy.PropertyNamingStrategyBase naming = (PropertyNamingStrategy.PropertyNamingStrategyBase) telegramApi.annotationType()
                .getAnnotation(JsonNaming.class)
                .value()
                .getConstructor()
                .newInstance();

        Arrays.stream(type.getDeclaredFields())
                .filter(field -> !isStatic(field.getModifiers()))
                .filter(field -> !field.isAnnotationPresent(Ignore.class))
                .map(field -> {
                    final String name = naming.translate(field.getName());
                    final MultipartParameter<?> value;

                    final Class<?> fieldType = field.getType();
                    final Optional<Object> fieldValue = getFieldValue(o, field);

                    if (Uploading.class.isAssignableFrom(fieldType))
                        value = fieldValue.map(v -> writeValueAsStream(name, (Uploading) v)).orElseThrow();
                    else
                        value = fieldValue.map(v -> writeValueAsString(name, v)).orElseGet(() -> new InnerStringMultipartParameter(name, null));

                    return value;
                })
                .collect(Collectors.toCollection(() -> parameters));
    }

    @Data
    @Accessors(chain = true, fluent = true)
    private static class InnerStreamMultipartParameter implements StreamMultipartParameter {
        private final String key;
        private final FileLoader value;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    private static class InnerStringMultipartParameter implements SimpleMultipartParameter {
        private final String key;
        private final String value;
    }

    @SneakyThrows
    private Optional<Object> getFieldValue(@Nonnull Object object, @Nonnull Field field) {
        field.setAccessible(true);
        return Optional.ofNullable(field.get(object));
    }

    @SneakyThrows
    private SimpleMultipartParameter writeValueAsString(@Nonnull String key, @Nonnull Object object) {
        final String value;

        final Class<?> type = object.getClass();

        if (type.isAnnotationPresent(Json.class))
            value = mapper.writeValueAsString(object);
        else
            value = object.toString();

        return new InnerStringMultipartParameter(key, value);
    }


    @SneakyThrows
    private StreamMultipartParameter writeValueAsStream(@Nonnull String key, @Nonnull Uploading uploading) {
        return new InnerStreamMultipartParameter(key, Objects.requireNonNull(uploading.uploadNewFile()));
    }

    @Override
    public Request.HttpMethod method() {
        return MULTIPART;
    }
}
