package com.goodboy.telegram.bot.spring;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.env.Environment;

import javax.annotation.Nonnull;
import java.io.InputStream;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ApplicationPropertyCertificateProvider implements CertificateProvider {

    private final Environment environment;

    @SneakyThrows
    public byte[] certificate(@Nonnull String botName) {
        @Nullable String certificatePath = TelegramBotConfigurableDefinitions.BotDefinition.getCertificatePath(botName, environment);

        if(StringUtils.isNotEmpty(certificatePath)){
            InputStream stream = ApplicationPropertyCertificateProvider.class.getClassLoader().getResourceAsStream(certificatePath);

            if(stream != null)
                return stream.readAllBytes();
        }

        return null;
    }
}
