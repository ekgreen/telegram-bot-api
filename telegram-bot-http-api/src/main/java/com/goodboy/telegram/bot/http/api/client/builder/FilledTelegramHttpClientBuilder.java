package com.goodboy.telegram.bot.http.api.client.builder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.goodboy.telegram.bot.http.api.client.BaseTelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.adapter.Callback;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapter;
import com.goodboy.telegram.bot.http.api.client.adapter.HttpClientAdapterCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.get.GetCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.get.TelegramApiToAdapterGetRequestMapper;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.MultipartCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.multipart.TelegramApiToAdapterMultipartRequestMapper;
import com.goodboy.telegram.bot.http.api.client.adapter.post.PostCallback;
import com.goodboy.telegram.bot.http.api.client.adapter.post.TelegramApiToAdapterPostRequestMapper;
import com.goodboy.telegram.bot.http.api.client.configuration.PropertyTelegramApiConfiguration;
import com.goodboy.telegram.bot.http.api.client.configuration.TelegramApiConfiguration;
import com.goodboy.telegram.bot.http.api.client.configuration.TelegramHttpClientProperties;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpCommandBasedHandler;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpFileBasedHandler;
import com.goodboy.telegram.bot.http.api.client.handlers.HttpRequestTypeBasedHandler;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.http.api.client.token.TelegramApiTokenResolver;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FilledTelegramHttpClientBuilder implements TelegramHttpClientBuilder {

    // default object mapper - single instance on all calls
    private final static Supplier<ObjectMapper> OBJECT_MAPPER_FACTORY = Suppliers.memoize(FilledTelegramHttpClientBuilder::telegramBotServiceObjectMapper);

    // necessary handlers
    private final Supplier<Map<RequestType,HttpRequestTypeBasedHandler>> CLIENT_HANDLERS_FACTORY = Suppliers.memoize(this::telegramBotServiceHandlers);

    // necessary mappers
    private final Supplier<Map<Request.HttpMethod, HttpClientAdapterCallback>> CLIENT_MAPPERS_FACTORY = Suppliers.memoize(this::telegramBotServiceMappers);

    // Пользовательский object mapper
    private ObjectMapper objectMapper;

    // Набор настроек для клиента
    private TelegramApiConfiguration configuration;

    // Адаптер для http клиентов (сторонних библиотек) которые могли обработать все необходимые типы запросов
    private List<HttpClientAdapterCallback> mappers;

    //  Обработчик на получение имени метода
    private List<HttpRequestTypeBasedHandler> handlers;

    // Токенизатор - динамическое получение токена, из контекста
    private TelegramApiTokenResolver tokenResolver;


    @Override
    public TelegramHttpClientBuilder objectMapper(@NotNull ObjectMapper mapper) {
        Objects.requireNonNull(mapper, "mapper cannot be null");
        this.objectMapper = mapper;
        return this;
    }

    @Override
    public TelegramHttpClientBuilder token(@NotNull String token) {
        Objects.requireNonNull(token, "token cannot be null");
        this.tokenResolver = () -> token;
        return this;
    }

    @Override
    public TelegramHttpClientBuilder token(@NotNull TelegramApiTokenResolver resolver) {
        Objects.requireNonNull(resolver, "resolver cannot be null");
        this.tokenResolver = resolver;
        return this;
    }

    @Override
    public TelegramHttpClientBuilder configuration(@NotNull TelegramApiConfiguration configuration) {
        Objects.requireNonNull(configuration, "configuration cannot be null");
        this.configuration = configuration;
        return this;
    }

    @Override
    public TelegramHttpClientBuilder callback(@NotNull HttpClientAdapterCallback callback) {
        Objects.requireNonNull(callback, "callback cannot be null");
        if(mappers == null)
            mappers = new ArrayList<>();

        mappers.add(callback);
        return this;
    }

    @Override
    public TelegramHttpClientBuilder callbacks(@NotNull List<HttpClientAdapterCallback> callbacks) {
        Objects.requireNonNull(callbacks, "callbacks cannot be null");
        if(mappers == null)
            mappers = new ArrayList<>();

        mappers.addAll(callbacks);
        return this;
    }

    @Override
    public TelegramHttpClientBuilder requestHandler(@NotNull HttpRequestTypeBasedHandler handler) {
        Objects.requireNonNull(handler, "handler cannot be null");
        if(handlers == null)
            handlers = new ArrayList<>();

        handlers.add(handler);
        return this;
    }

    @Override
    public TelegramHttpClientBuilder requestHandlers(@NotNull List<HttpRequestTypeBasedHandler> handlers) {
        Objects.requireNonNull(handlers, "handlers cannot be null");
        if(this.handlers == null)
            this.handlers = new ArrayList<>();

        this.handlers.addAll(handlers);
        return this;
    }

    @Override
    public TelegramHttpClient build(@NotNull HttpClientAdapter adapter) {
        return new BaseTelegramHttpClient(
                adapter,
                buildConfiguration(),
                buildMappers(),
                buildHandlers(),
                buildToken()
        );
    }

    private @Nonnull TelegramApiConfiguration buildConfiguration() {
        return configuration != null ? configuration : new PropertyTelegramApiConfiguration(new TelegramHttpClientProperties());
    }

    private @Nonnull List<HttpClientAdapterCallback> buildMappers() {
        final Map<Request.HttpMethod, HttpClientAdapterCallback> defaultMappers = CLIENT_MAPPERS_FACTORY.get();

        if(mappers == null || mappers.isEmpty())
            return ImmutableList.copyOf(defaultMappers.values());

        // check consistency
        final Set<Request.HttpMethod> typesPresent = mappers.stream().map(HttpClientAdapterCallback::method).collect(Collectors.toSet());

        defaultMappers.forEach( (key,value) -> {
            if(!typesPresent.contains(key))
                mappers.add(value);
        });

        return mappers;
    }

    private @Nonnull List<HttpRequestTypeBasedHandler> buildHandlers() {
        final Map<RequestType, HttpRequestTypeBasedHandler> defaultHandlers = CLIENT_HANDLERS_FACTORY.get();

        if(handlers == null || handlers.isEmpty())
            return ImmutableList.copyOf(defaultHandlers.values());

        // check consistency
        final Set<RequestType> typesPresent = handlers.stream().map(HttpRequestTypeBasedHandler::type).collect(Collectors.toSet());

        defaultHandlers.forEach( (key,value) -> {
            if(!typesPresent.contains(key))
                handlers.add(value);
        });

        return handlers;
    }

    private @Nonnull TelegramApiTokenResolver buildToken() {
        return tokenResolver == null ? () -> null : tokenResolver;
    }

    private Map<Request.HttpMethod, HttpClientAdapterCallback> telegramBotServiceMappers() {
        final ObjectMapper objectMapper = getObjectMapper();

        return ImmutableMap.of(
                Request.HttpMethod.GET, new GetCallback(new TelegramApiToAdapterGetRequestMapper(objectMapper)),
                Request.HttpMethod.POST, new PostCallback(new TelegramApiToAdapterPostRequestMapper(objectMapper)),
                Request.HttpMethod.MULTIPART, new MultipartCallback(new TelegramApiToAdapterMultipartRequestMapper(objectMapper))
        );
    }

    private Map<RequestType,HttpRequestTypeBasedHandler> telegramBotServiceHandlers() {
        return ImmutableMap.of(
                RequestType.COMMAND, new HttpCommandBasedHandler(getObjectMapper()),
                RequestType.FILE, new HttpFileBasedHandler()
        );
    }


    protected @Nonnull ObjectMapper getObjectMapper() {
        return objectMapper != null ? objectMapper : OBJECT_MAPPER_FACTORY.get();
    }

    private static ObjectMapper telegramBotServiceObjectMapper() {
        return new ObjectMapper()
                .enable(
                        DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                        DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
                        DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                        DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,
                        DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS
                )
                .enable(
                        JsonParser.Feature.ALLOW_SINGLE_QUOTES
                )
                .disable(
                        DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
                        DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS
                )
                .disable(
                        SerializationFeature.FAIL_ON_EMPTY_BEANS,
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
                )
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                ;
    }
}
