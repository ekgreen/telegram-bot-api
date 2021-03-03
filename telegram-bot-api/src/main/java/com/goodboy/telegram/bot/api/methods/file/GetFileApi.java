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
 * https://api.telegram.org/file/bot<token>/<file_path>, where <file_path> is taken from the response. It is guaran-
 * teed that the link will be valid for at least 1 hour. When the link expires, a new one can be requested by calling
 * getFile again.
 */
@Data
@TelegramApi
@Accessors(chain = true)
public class GetFileApi implements Api {

    /**
     * 	File identifier to get info about
     */
    private String fileId;
}
