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

package com.goodboy.telegram.bot.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TelegramApiDtoMetadataDescription {

    /**
     * Unique message identifier inside this chat
     */
    public static final String MESSAGE_ID_DESCRIPTOR = "message_id";

    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    public static final String FILE_ID_DESCRIPTOR = "file_id";

    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots
     */
    public static final String UNIQUE_FILE_ID_DESCRIPTOR = "file_unique_id";
}
