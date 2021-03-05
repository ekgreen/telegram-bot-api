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

package com.goodboy.telegram.bot.example.service;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Service
public class HiBrownieCleaningService implements BrownieCleaningService {

    private final static String[] LOCATIONS = {
            "kitchen", "bathroom", "living room", "bedroom"
    };

    public @Nonnull String sayHi(String sender, String message) {
        final String place = LOCATIONS[(int) (4 * Math.random())];

        return String.format("Hi %s, im really enjoy to answer u on message [%s], but now I'm cleaning %s", sender, message, place);
    }
}
