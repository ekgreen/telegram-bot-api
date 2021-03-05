/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goodboy.telegram.bot.api.keyboard;

/**
 * Traditional chat bots can of course be taught to understand human language.
 * But sometimes you want some more formal input from the user — and this is where custom
 * keyboards can become extremely useful.
 *
 * Whenever your bot sends a message, it can pass along a special keyboard with predefined
 * reply options (see ReplyKeyboardMarkup). Telegram apps that receive the message
 * will display your keyboard to the user. Tapping any of the buttons will immediately
 * send the respective command. This way you can drastically simplify user interaction
 * with your bot.
 *
 * This is marker interface
 *
 * @author Izmalkov Roman (ekgreen)
 * @since 1.0.0
 */
public interface ReplyMarkup {}
