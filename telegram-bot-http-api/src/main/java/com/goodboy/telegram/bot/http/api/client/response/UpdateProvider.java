package com.goodboy.telegram.bot.http.api.client.response;

import com.goodboy.telegram.bot.api.Update;

public interface UpdateProvider {

    Update getUpdate();
}
