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

package com.goodboy.telegram.bot.api.methods.file;

import com.goodboy.telegram.bot.api.BotCommand;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.methods.Api;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Use this method to get basic info about a file and prepare it for downloading. For the moment, bots can download
 * files of up to 20MB in size. On success, a File object is returned. The file can then be downloaded via the link
 * https://api.telegram.org/file/bot{{token}}/{{file_path}}, where {{file_path}} is taken from the response. It is guaran-
 * teed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling
 * getFile again.
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
@Data
@TelegramApi
@Accessors(chain = true)
public class GetFileApi implements Api {

    /**
     * File identifier to get info about
     */
    private String fileId;
}
