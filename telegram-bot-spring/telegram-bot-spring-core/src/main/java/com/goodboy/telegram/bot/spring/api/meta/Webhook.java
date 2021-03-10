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

package com.goodboy.telegram.bot.spring.api.meta;

import com.goodboy.telegram.bot.api.methods.action.Action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Webhook {

    /**
     * The commands which supports webhook (part of API Telegram)
     */
    String[] command() default {};

    /**
     * Type of webhook execution
     */
    ExecutionType type() default ExecutionType.LIGHTWEIGHT;

    /**
     * Type of action to broadcast. Choose one, depending on what the user is about to receive: typing for text
     * messages, upload_photo for photos, record_video or upload_video for videos, record_voice or upload_voice
     * for voice notes, upload_document for general files, find_location for location data, record_video_note or
     * upload_video_note for video notes.
     *
     * Use only with {@link ExecutionType#HEAVYWEIGHT}
     *
     * @return fast action
     */
    Action[] action() default {};

    /**
     * @return name of bean for heavy weight execution or will executed in default
     */
    String heavyWeightExecutorService() default "";

    public enum ExecutionType{
        /**
         * means that method is light and execution might be on http life-connection
         */
        LIGHTWEIGHT,
        /**
         * means that method is unstable and execution type have to be calculated automatically
         */
        SNEAKY, // Note: not supported yet
        /**
         * means that method is heavy and http connection should not hang
         */
        HEAVYWEIGHT
    }
}
