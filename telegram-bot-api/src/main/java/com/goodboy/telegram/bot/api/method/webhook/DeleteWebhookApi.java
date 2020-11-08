package com.goodboy.telegram.bot.api.method.webhook;

import com.goodboy.telegram.bot.api.meta.Optional;
import com.goodboy.telegram.bot.api.meta.TelegramApi;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@TelegramApi
@Data
@Accessors(chain = true)
public class DeleteWebhookApi {}
