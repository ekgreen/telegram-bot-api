package com.goodboy.telegram.bot.spring.providers;

import javax.annotation.Nonnull;

public interface CertificateProvider {
    byte[] certificate(@Nonnull String botName);
}
