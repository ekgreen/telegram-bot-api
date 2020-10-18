package com.goodboy.telegram.bot.core.method.message;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.client.TelegramHttpClient;
import com.goodboy.telegram.bot.api.exception.TelegramApiExceptionDefinitions;
import com.goodboy.telegram.bot.api.method.message.*;
import com.goodboy.telegram.bot.api.method.token.TokenSupplier;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

@Setter
@RequiredArgsConstructor
public class TelegramMessageApiAdapter implements TelegramMessageApi {

    private final TelegramHttpClient client;
    private TokenSupplier tokenizer = () -> null;

    public @NotNull TelegramCoreResponse<Message> sendMessage(@NotNull SendMessageApi api) {
        return sendMessage((request) -> request
                .setToken(tokenizer.get())
                .setBody(api)
        );
    }

    public @NotNull TelegramCoreResponse<Message> sendPhoto(@NotNull SendPhotoApi api) {
        return sendPhoto((request) -> request
                .setToken(tokenizer.get())
                .setBody(api)
        );
    }

    public @NotNull TelegramCoreResponse<Message> sendAudio(@NotNull SendAudioApi api) {
        return sendAudio((request) -> request
                .setToken(tokenizer.get())
                .setBody(api)
        );
    }

    public @NotNull TelegramCoreResponse<Message> sendDocument(@NotNull SendDocumentApi api) {
        return sendDocument((request) -> request
                .setToken(tokenizer.get())
                .setBody(api)
        );
    }

    public @NotNull TelegramCoreResponse<Message> sendVideo(@NotNull SendVideoApi api) {
        return sendVideo((request) -> request
                .setToken(tokenizer.get())
                .setBody(api)
        );
    }

    public @NotNull TelegramCoreResponse<Message> sendAnimation(@NotNull SendAnimationApi api) {
        return sendAnimation((request) -> request
                .setToken(tokenizer.get())
                .setBody(api)
        );
    }

    public @NotNull TelegramCoreResponse<Message> sendLocation(@NotNull SendLocationApi api) {
        return sendLocation((request) -> request
                .setToken(tokenizer.get())
                .setBody(api)
        );
    }


    public @NotNull TelegramCoreResponse<Message> sendSticker(@NotNull SendStickerApi api) {
        return sendSticker((request) -> request
                .setToken(tokenizer.get())
                .setBody(api)
        );
    }

    public @NotNull TelegramCoreResponse<Message> sendMessage(Consumer<Request<SendMessageApi>> handler) {
        return handleTelegramRequest(TelegramMessageApiDefinitions.SEND_MESSAGE_CALL_METHOD, handler);
    }

    public @NotNull TelegramCoreResponse<Message> sendPhoto(Consumer<Request<SendPhotoApi>> handler) {
        return handleTelegramRequest(TelegramMessageApiDefinitions.SEND_MESSAGE_CALL_METHOD, handler);
    }

    public @NotNull TelegramCoreResponse<Message> sendAudio(Consumer<Request<SendAudioApi>> handler) {
        return handleTelegramRequest(TelegramMessageApiDefinitions.SEND_MESSAGE_CALL_METHOD, handler);
    }

    public @NotNull TelegramCoreResponse<Message> sendDocument(Consumer<Request<SendDocumentApi>> handler) {
        return handleTelegramRequest(TelegramMessageApiDefinitions.SEND_MESSAGE_CALL_METHOD, handler);
    }

    public @NotNull TelegramCoreResponse<Message> sendVideo(Consumer<Request<SendVideoApi>> handler) {
        return handleTelegramRequest(TelegramMessageApiDefinitions.SEND_MESSAGE_CALL_METHOD, handler);
    }

    public @NotNull TelegramCoreResponse<Message> sendAnimation(Consumer<Request<SendAnimationApi>> handler) {
        return handleTelegramRequest(TelegramMessageApiDefinitions.SEND_MESSAGE_CALL_METHOD, handler);
    }

    public @NotNull TelegramCoreResponse<Message> sendLocation(Consumer<Request<SendLocationApi>> handler) {
        return handleTelegramRequest(TelegramMessageApiDefinitions.SEND_MESSAGE_CALL_METHOD, handler);
    }

    public @NotNull TelegramCoreResponse<Message> sendSticker(Consumer<Request<SendStickerApi>> handler) {
        return handleTelegramRequest(TelegramMessageApiDefinitions.SEND_MESSAGE_CALL_METHOD, handler);
    }

    private <T> TelegramCoreResponse<Message> handleTelegramRequest(@Nonnull String callName, @Nonnull Consumer<Request<T>> handler) {
        final Request<T> request = new Request<T>()
                .setCallName(callName)
                .setResponseType(Message.class);

        handler.accept(request);

        return client.send(request);
    }

}
