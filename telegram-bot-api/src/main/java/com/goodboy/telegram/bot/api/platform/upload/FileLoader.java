package com.goodboy.telegram.bot.api.platform.upload;

import javax.annotation.Nonnull;

public interface FileLoader {

    @Nonnull byte[] bytes();

    @Nonnull String fileName();
}
