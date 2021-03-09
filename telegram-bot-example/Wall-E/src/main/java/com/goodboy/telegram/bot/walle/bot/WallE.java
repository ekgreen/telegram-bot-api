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

package com.goodboy.telegram.bot.walle.bot;

import com.goodboy.telegram.bot.api.Update;
import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.methods.message.SendMessageApi;
import com.goodboy.telegram.bot.spring.api.actions.FastAction;
import com.goodboy.telegram.bot.spring.api.actions.Solo;
import com.goodboy.telegram.bot.spring.api.actions.VoidApi;
import com.goodboy.telegram.bot.spring.api.meta.Bot;
import com.goodboy.telegram.bot.spring.api.meta.ChatId;
import com.goodboy.telegram.bot.spring.api.meta.Nickname;
import com.goodboy.telegram.bot.spring.api.meta.Webhook;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

import java.util.concurrent.TimeUnit;

import static com.goodboy.telegram.bot.api.methods.action.Action.TYPING;
import static com.goodboy.telegram.bot.spring.api.meta.Webhook.ExecutionType.HEAVYWEIGHT;

/**
 * Somebody: Where are we going?
 * Wall-E:   In the space <3
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Slf4j
@Bot(value = "walle", path = "/walle")
public class WallE {

    @Webhook(command = "talk")
    public void walleListening(@Nonnull Update update) {
        log.info("Are u talking to me? {}", update);
    }

    @Webhook(type = HEAVYWEIGHT, action = TYPING)
    public @SneakyThrows Api hardTyping(@ChatId String chatId, @Nickname String nickname) {
        TimeUnit.SECONDS.sleep(2);
        return new SendMessageApi()
                .setChatId(chatId)
                .setText("I dream of space but for now I'm with you " + ( nickname != null ? nickname : "the stranger" ));
    }

    @Webhook(command = "talk")
    public Solo walleListening2(@Nonnull Update update) {
        return () -> {};
    }
}
