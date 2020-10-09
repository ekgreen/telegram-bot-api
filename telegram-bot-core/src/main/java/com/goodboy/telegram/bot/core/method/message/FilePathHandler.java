package com.goodboy.telegram.bot.core.method.message;

import com.goodboy.telegram.bot.api.client.PathHandler;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;

public class FilePathHandler implements PathHandler<byte[]> {

    @Override
    public TelegramCoreResponse<byte[]> handle(byte[] response) {
        return new FileTelegramResponse(response);
    }

    @Override
    public String handlingOn() {
        return "/file";
    }

    private static class FileTelegramResponse extends TelegramCoreResponse<byte[]>{
        private FileTelegramResponse(byte[] response) {
            setOk(response != null);
            setResult(response);
        }
    }
}
