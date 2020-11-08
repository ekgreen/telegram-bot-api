package com.goodboy.telegram.bot.spring.impl.providers;

import com.goodboy.telegram.bot.spring.api.environment.TelegramEnvironment;
import com.goodboy.telegram.bot.spring.api.providers.CertificateProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ApplicationPropertyCertificateProvider implements CertificateProvider {

    private final TelegramEnvironment environment;

    @SneakyThrows
    public byte[] certificate(@NotNull String botName) {
        @Nullable String certificatePath = environment.getBotByName(botName).getWebhook().getCertificate();

        if(StringUtils.isNotEmpty(certificatePath)){
            InputStream stream = ApplicationPropertyCertificateProvider.class.getClassLoader().getResourceAsStream(certificatePath);

            if(stream == null)
                stream = Files.newInputStream(Paths.get(certificatePath));

            return stream.readAllBytes();
        }

        return null;
    }
}
