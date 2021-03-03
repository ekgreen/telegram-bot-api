package com.goodboy.telegram.bot.http.api.client.token;

import java.util.function.Supplier;

/**
 * Интерфейс преднозначен для динамического получения токена,
 * например токен можно получить из файла или стороннего ресурса,
 * отвечающего за приватные данные
 *
 * Интерфейс расширяет базовый интерфейс {@link Supplier}, типизирую его
 * под вид токена
 */
public interface TelegramApiTokenResolver extends Supplier<String> {}
