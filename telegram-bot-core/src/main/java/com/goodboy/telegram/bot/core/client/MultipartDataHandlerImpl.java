package com.goodboy.telegram.bot.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.goodboy.telegram.bot.api.meta.Ignore;
import com.goodboy.telegram.bot.api.meta.Json;
import com.goodboy.telegram.bot.api.meta.Multipart;
import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.client.adapter.MultipartHttpClientHandler;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.exception.TelegramApiRuntimeException;
import com.goodboy.telegram.bot.api.request.ContentDispositionUploading;
import com.goodboy.telegram.bot.api.request.StreamUploading;
import com.goodboy.telegram.bot.api.request.StringUploading;
import com.goodboy.telegram.bot.api.request.Uploading;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.*;

@RequiredArgsConstructor
public class MultipartDataHandlerImpl implements MultipartHttpClientHandler {

    private final static Set<Predicate<Field>> filters = Sets.newHashSet(
            field -> !isStatic(field.getModifiers()),
            field -> !field.isAnnotationPresent(Ignore.class)
    );

    private final ObjectMapper mapper;

    @SneakyThrows
    public <V> Iterable<Part<?>> parts(Request<V> request) {
        @NotNull V body = Objects.requireNonNull(request.getBody());

        final Class<?> type = body.getClass();

        if (!condition().test(request))
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, "missed multipart annotation on handling multipart request");

        final Multipart annotation = type.getAnnotation(Multipart.class);

        final PropertyNamingStrategy.PropertyNamingStrategyBase namingStrategy = annotation.strategy().getConstructor().newInstance();

        return Arrays.stream(type.getDeclaredFields()).filter(this::filter).map(field -> {
            try {
                final String name = namingStrategy.translate(field.getName());

                field.setAccessible(true);
                final Class<?> fieldType = field.getType();
                Object fieldValue = field.get(body);

                if (fieldValue == null)
                    return null;

                if (Uploading.class.isAssignableFrom(fieldType)) {
                    return uploadingPart(name, fieldValue);
                } else {
                    return anyFieldWithCustoms(name, field, fieldValue);
                }
            } catch (Exception exception) {
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, exception);
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Part<?> uploadingPart(@NotNull String name, @NotNull Object fieldValue) {
        final Class<?> fieldType = fieldValue.getClass();

        if (StringUploading.class.isAssignableFrom(fieldType))
            return new StringPart(name, ((StringUploading) fieldValue).uploading());

        if (StreamUploading.class.isAssignableFrom(fieldType)) {

            final StreamUploading<?> uploading = (StreamUploading<?>) fieldValue;

            return new StreamPart(name, uploading.uploadingName(), createUploadingLoader(uploading));
        }

        if(ContentDispositionUploading.class.isAssignableFrom(fieldType)){
            final ContentDispositionUploading<?> uploading = (ContentDispositionUploading<?>) fieldValue;

            return new ContentPart(createContentDisposition(uploading), createUploadingLoader(uploading));
        }

        throw new IllegalStateException("illegal uploading class = " + fieldType);
    }

    private String createContentDisposition(@NotNull ContentDispositionUploading<?> uploading) {
        return "form-data; " + uploading.disposition();
    }

    private Supplier<byte[]> createUploadingLoader(Uploading<? extends Supplier<?>> uploading) {
        return () -> {
            try {
                final Object data = uploading.uploading().get();

                if (data == null)
                    return null;

                final Class<?> dataType = data.getClass();

                byte[] array;

                if (InputStream.class.isAssignableFrom(dataType)) {
                    array = ((InputStream) data).readAllBytes();
                } else if (byte[].class.isAssignableFrom(dataType)) {
                    array = (byte[]) data;
                } else
                    throw new IllegalStateException("illegal uploading class = " + dataType);

                return array;
            } catch (Exception exception) {
                throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.HTTP_REQUEST_ERROR, exception);
            }
        };
    }

    @SneakyThrows
    private Part<String> anyFieldWithCustoms(@NotNull String name, @NotNull Field fieldType, @NotNull Object fieldValue) {
        boolean map = fieldType.isAnnotationPresent(Json.class);

        String data;

        if (map) {
            data = mapper.writeValueAsString(fieldValue);
        } else {
            data = fieldValue.toString();
        }

        return new StringPart(name, data);
    }

    @Override
    public Predicate<Request<?>> condition() {
        return request -> request != null && request.getBody() != null && request.getBody().getClass().isAnnotationPresent(Multipart.class);
    }

    private boolean filter(Field field) {
        return filters.stream().allMatch(predicate -> predicate.test(field));
    }

}
