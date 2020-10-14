package com.goodboy.telegram.bot.spring;

import javax.annotation.Nonnull;

public interface CertificateProvider {
    byte[] certificate(@Nonnull String botName);
}
