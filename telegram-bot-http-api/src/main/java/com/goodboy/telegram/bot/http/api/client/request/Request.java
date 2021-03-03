package com.goodboy.telegram.bot.http.api.client.request;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface Request<V> {

    /**
     * @return данные о вызываемом удаленном методе
     */
    @Nonnull CallMethod callMethod();

    /**
     * @return тело запроса
     */
    Optional<V> payload();

    /**
     * @return токен от чьего лица отправляется запрос
     */
    Optional<String> token();


    public interface CallMethod{

        /**
         * @return название вызываемого метода
         */
        @Nonnull String name();

        /**
         * @return тип вызываемого метода
         */
        @Nonnull
        RequestType type();

        /**
         * @return предполагаемый возвращаемый тип
         */
        @Nonnull Class<?> desireReturnType();

        /**
         * @return желательный способ отправки данных
         */
        Optional<HttpMethod> desireHttpMethod();
    }

    public enum HttpMethod {
        GET, POST, MULTIPART
    }

    public interface CallMethodBuilder<V>{

        CallMethodBuilder<V> withName(@Nonnull String name);
        CallMethodBuilder<V> withType(@Nonnull RequestType type);
        CallMethodBuilder<V> withHttpMethod(@Nonnull HttpMethod httpMethod);
        CallMethodBuilder<V> withReturnType(@Nonnull Class<?> returnType);

        RequestBuilder<V> build();
    }

    public interface RequestBuilder<V>{

        CallMethodBuilder<V> withCallMethod();
        RequestBuilder<V> withPayload(@Nonnull V payload);
        RequestBuilder<V> withToken(@Nonnull String token);

        @Nonnull Request<V> build();
    }
}
