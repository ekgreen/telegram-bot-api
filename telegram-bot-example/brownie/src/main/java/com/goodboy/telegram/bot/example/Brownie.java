/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.example;

import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.methods.message.SendMessageApi;
import com.goodboy.telegram.bot.example.service.BrownieCleaningService;
import com.goodboy.telegram.bot.spring.api.meta.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Nonnull;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Bot(value = "brownie", path = "/brownie")
public class Brownie {

    private @Autowired
    BrownieCleaningService service;

    @PostMapping(path = "/admin")
    public String botAdministrationMethod() {
        return "bot admin here!";
    }

    @Webhook
    public @Nonnull Api onUpdate(
            @ChatId String chatId,
            @Nickname String nickname,
            @MessageText String text
    ) {
        return new SendMessageApi()
                .setChatId(chatId)
                .setText(service.sayHi(nickname, text));
    }

}
