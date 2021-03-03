package com.goodboy.telegram.bot.api.methods.webhook;

import com.goodboy.telegram.bot.api.meta.TelegramApi;
import com.goodboy.telegram.bot.api.methods.Api;
import lombok.Data;
import lombok.experimental.Accessors;

@TelegramApi
@Data
@Accessors(chain = true)
public class GetWebhookApi implements Api {}
