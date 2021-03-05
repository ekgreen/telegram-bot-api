# <img src="docs/bender.png" width="80" height="95"> Telegram Bot Framework [![Build Status](https://travis-ci.org/ekgreen/telegram-bot-api.svg?branch=master)](https://travis-ci.org/pengrad/java-telegram-bot-api) [![codecov](https://codecov.io/gh/ekgreen/telegram-bot-api/branch/master/graph/badge.svg)](https://codecov.io/gh/pengrad/java-telegram-bot-api)

*Read this in other languages: [Russian](README.ru.md)*

* [Обзор](#Обзор)
* [Клиент](#Клиент)
    - [Начало работы](##`Начало работы`)
	    - [Создание клиента](###`Создание клиента`)
	    - [Примеры вызовов](###Примеры)
    - [Развитие](##Развитие)
* [Сервер](#Сервер)
    - [Начало работы](##`Начало работы`)
	    - [Создание клиента](###`Создание клиента`)
	    - [Примеры вызовов](###Примеры)
	- [Развитие](##Развитие)
* [Лицензия](#Лицензия)

# Обзор

Репозиторий содержит в себе набор  **java** библиотек, которые могут помочь вам в работе с [Telegram Bot API](https://core.telegram.org/bots/api),
сейчас они разделены на две основные части:
- Расширяемый интерфейс для отправки синхронных и асинхронных запросов к API или [TelegramHttpClient](telegram-bot-http-api/src/main/java/com/goodboy/telegram/bot/http/api/client/TelegramHttpClient.java);
- Интеграция со [Spring Boot](https://github.com/spring-projects/spring-boot).

Клиент не зависит от интеграции со Spring Boot - его можно использовать отдельно.

# Клиент

Gradle:
```groovy
// api
api 'com.github.ekgreen:telegram-bot-http-api:0.0.1-SNAPSHOT'
// ok http client implementation
implementation 'com.github.ekgreen:telegram-bot-ok-http-client:0.0.1-SNAPSHOT'
```
Maven:
```xml
<!-- api -->
<dependency>
  <groupId>com.github.ekgreen</groupId>
  <artifactId>telegram-bot-http-api</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <scope>provided</scope>
</dependency>
<!-- ok http client implementation -->
<dependency>
  <groupId>com.github.ekgreen</groupId>
  <artifactId>telegram-bot-ok-http-client</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Клиент в первой своей итерации решает задачи не связанные с оборачиванием в фантик [Telegram Bot API](https://core.telegram.org/bots/api),
а нацелен на привнесение гибкости в процесс разработки. API [TelegramHttpClient](telegram-bot-http-api/src/main/java/com/goodboy/telegram/bot/http/api/client/BaseTelegramHttpClient.java)
расширяем по трем основным направлениям (вообще их пять, но два другие направления ближе к 'кишкам' реализации клиента, чем к повседневным настройкам):
* Вы можете использовать любой http клиент, если реализуете под него [HttpClientAdapter](telegram-bot-http-api/src/main/java/com/goodboy/telegram/bot/http/api/client/adapter/HttpClientAdapter.java).
В качестве дефолтной реализации в проекте есть имплементация интерфейса для [OkHttpClient](https://square.github.io/okhttp/), а именно 
[OkHttpClientAdapter](telegram-bot-ok-http-client/src/main/java/com/goodboy/telegram/bot/http/client/OkHttpClientAdapter.java);
* [Конфигурировать](telegram-bot-http-api/src/main/java/com/goodboy/telegram/bot/http/api/client/configuration/TelegramApiConfiguration.java) дополнительные настройки для клиента;
* Динамически или статически полученать токен/токены - за пользователем остается возможность выбрать способ получения токена или токенов в зависимости от конкретной ситуации.

## Начало работы
Все что нужно нужно для начала работы с клиентом - это его создать :) Предусмотрено несколько дефолтных билдеров создание
которых не займет много времени.
### Создание клиента:
```java
// минимально возможная реализация в библиотеке с api клиента
final TelegramHttpClient client = TelegramHttpClientBuilder.defaultLesslessTelegramHttpClient(adapter);
// минимальная реализация с привязкой к конкретному боту
final TelegramHttpClient client = TelegramHttpClientBuilder.defaultTiedTelegramHttpClient(token,adapter);
// Данный билдер находится в библиотеке с расширением для OkHttpClient - билдер самодостаточный, ему не требуется 
// передавать на вход адаптер; используется дефолтный адаптер для данной имплементации
final TelegramHttpClient client = new OkHttpFilledTelegramHttpClientBuilder()
    .token(token)
    .build();
```

### Примеры
Получение информации о боте [[getMe]](https://core.telegram.org/bots/api#getme) используя сервисы API Telegram
```java
    @Test
    public void sendMessageWithApi() {
        // simple call
        final TelegramHttpClient client = new OkHttpFilledTelegramHttpClientBuilder()
                .token(getToken())
                .build();

        // create getMe service
        // use api is most simple way to call telegram bot api - api hides from user inner implementation
        // of telegram bot http client
        final var api = new TelegramMeImpl(client);

        // request to rich info about bot
        // all responses wraps in uniform telegram api response
        TelegramCoreResponse<User> response = api.getMe();

        // assert that it is success call
        Assertions.assertTrue(response.isOk());
        // my test bot name is 10words - here no logic
        Assertions.assertEquals("10words", response.getResult().getFirstName());
    }    
```
Получение информации о боте [[getMe]](https://core.telegram.org/bots/api#getme) используя телеграм клиент
```java
    @Test
    public void sendMessageWithClientTest() {
        // this example show some entrails of client implementation
        final TelegramHttpClient client = new OkHttpFilledTelegramHttpClientBuilder()
                .token(getToken())
                .build();


        // request to rich info about bot
        // all responses wraps in uniform telegram api response
        final TelegramCoreResponse<User> response = client.send(
                // method request is description of calling method with body and environment such as token
                // NOTE! that we already created client implementation tied to direct bot and it is not necessary
                // provide token on each call
                new MethodRequest<>(
                        // technical calling method description
                        // - calling telegram api name
                        // - type of calling api
                        // - expected returning type
                        new CallMethodImpl(TelegramMethodApiDefinition.GET_ME, RequestType.COMMAND, User.class)
                )
        );

        // assert that it is success call
        Assertions.assertTrue(response.isOk());
        // my test bot name is 10words - here no logic
        Assertions.assertEquals("10words", response.getResult().getFirstName());
    }
```
## Развитие

Развитие клиента предусматривается по двум направлениям:
* Оборачивание API в более user-friendly сервисы для минимизации пользовательского кода и повышения уровня 'комфорта' при работе с библиотекой;
* Имплементация всего Telegram API в базовых API сервисах, например как [TelegramMeApi](telegram-bot-api/src/main/java/com/goodboy/telegram/bot/api/methods/me/TelegramMeApi.java);
* Покрытие клиента unit-тестами.

# Сервер