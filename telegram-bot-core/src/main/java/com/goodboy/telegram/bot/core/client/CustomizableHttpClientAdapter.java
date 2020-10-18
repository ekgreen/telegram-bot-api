package com.goodboy.telegram.bot.core.client;

import com.goodboy.telegram.bot.api.client.HttpClientAdapter;

public interface CustomizableHttpClientAdapter extends HttpClientAdapter {

    public CustomizableHttpClientAdapter multipartHandledClass(Class<?> type);

    public CustomizableHttpClientAdapter throwExceptionOnNonOkResponse(boolean throwExceptionOnNonOkResponse);
}
