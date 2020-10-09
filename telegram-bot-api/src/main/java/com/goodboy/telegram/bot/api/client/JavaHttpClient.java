package com.goodboy.telegram.bot.api.client;

public interface JavaHttpClient extends HttpClientAdapter{

    public <V> JavaHttpClient registryPublisher(HttpBodyPublisher<V> publisher);

    public JavaHttpClient intensiveJsonValidation(boolean intensiveJsonValidation);

    public JavaHttpClient usePostForEmptyRequests(boolean usePostMethodForEmptyRequests);

    public JavaHttpClient throwExceptionOnNonOkResponse(boolean throwExceptionOnNonOkResponse);
}
