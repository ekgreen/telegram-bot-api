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
import com.goodboy.telegram.bot.spring.api.meta.Bot;
import com.goodboy.telegram.bot.spring.api.meta.Webhook;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

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

    @Webhook
    public void walleListening(@Nonnull Update update) {
        log.info("Are u talking to me? {}", update);
    }
}
