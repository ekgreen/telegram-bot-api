package com.goodboy.telegram.bot.spring.api.providers;

import org.jetbrains.annotations.NotNull;

public interface CertificateProvider {
    byte[] certificate(@NotNull String botName);
}
