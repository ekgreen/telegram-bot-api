package com.goodboy.telegram.bot.http.api.client.extended;

import com.goodboy.telegram.bot.api.meta.ApiQuery;
import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.http.api.client.request.CallMethodImpl;
import com.goodboy.telegram.bot.http.api.client.request.MethodRequest;
import com.goodboy.telegram.bot.http.api.client.request.Request;
import com.goodboy.telegram.bot.http.api.client.request.RequestType;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.http.api.exception.TelegramApiRuntimeException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 *
 */
public class PathScanningExtendedTelegramClient implements ExtendedTelegramHttpClient {

    // base path for all modules of telegram api project
    private final static String DEFAULT_SCANNING_PATH = "com.goodboy.telegram.bot";

    private final Map<Class<?>, MethodRequest<Api>> requestDefinitionMap;
    private final TelegramHttpClient client;

    public PathScanningExtendedTelegramClient(@Nonnull TelegramHttpClient client, String... paths) {
        this.client = client;
        final List<String> candidates = Lists.newArrayList(DEFAULT_SCANNING_PATH);

        // todo - optimize by path comparing
        if (paths != null && paths.length > 0)
            candidates.addAll(List.of(paths));

        final Reflections scanner = new Reflections(candidates);

        this.requestDefinitionMap =
                ImmutableMap.copyOf(scanner.getTypesAnnotatedWith(ApiQuery.class).stream()
                        .filter(Api.class::isAssignableFrom)
                        .collect(toMap(
                                api -> api,
                                api -> {
                                    final @Nonnull ApiQuery query = api.getAnnotation(ApiQuery.class);
                                    // method request is description of calling method with body and environment such as token
                                    // NOTE! that we already created client implementation tied to direct bot and it is not necessary
                                    // provide token on each call
                                    return new MethodRequest<Api>(
                                            // technical calling method description
                                            // - calling telegram api name
                                            // - type of calling api
                                            // - expected returning type
                                            new CallMethodImpl(query.method(), getRequestTypeByPath(query.path()), query.provides())
                                    );
                                }
                        ))
                );
    }

    private static RequestType getRequestTypeByPath(@Nonnull String path) {
        final RequestType[] values = RequestType.values();

        for (RequestType value : values) {
            if(path.equals(value.getPath()))
                return value;
        }

        throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.TECHNICAL_EXCEPTION, "path not supported = " + path);
    }

    @Override
    public <T> TelegramCoreResponse<T> sendWithToken(@NotNull Api api, @Nullable String token) {
        final Class<? extends Api> apiType = api.getClass();

        if(!requestDefinitionMap.containsKey(apiType))
            throw new TelegramApiRuntimeException(TelegramApiExceptionDefinitions.VALIDATION_EXCEPTION, "api not supported by client = " + api);

        final MethodRequest<Api> request = createRequestDefinition(apiType);

        return client.send(request
                .setPayload(api)
                .setToken(token)
        );
    }

    private @Nonnull MethodRequest<Api> createRequestDefinition(@Nonnull Class<? extends Api> apiType) {
        final Request.CallMethod method = requestDefinitionMap.get(apiType).callMethod();

        return new MethodRequest<>(
                new CallMethodImpl(method.name(), method.type(), method.desireReturnType())
        );
    }

}
