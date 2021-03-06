package com.goodboy.telegram.bot.example;

import com.goodboy.telegram.bot.api.Message;
import com.goodboy.telegram.bot.api.StickerSet;
import com.goodboy.telegram.bot.api.methods.message.SendMessageApi;
import com.goodboy.telegram.bot.api.methods.message.stickers.GetStickerSetApi;
import com.goodboy.telegram.bot.api.methods.message.stickers.SendStickerApi;
import com.goodboy.telegram.bot.api.response.TelegramCoreResponse;
import com.goodboy.telegram.bot.http.api.client.extended.ExtendedTelegramHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SendMessageExample implements BotTest{

    @Test
    public void sendHelloToWallE() {
        // create extended telegram client
        final ExtendedTelegramHttpClient client = extendedClient();

        final String expected = "Hi, I am Wall-E bot. Do u want explore space with me?";

        // send message - receive message entity
        final TelegramCoreResponse<Message> response = client.send(new SendMessageApi()
                .setChatId(BotTest.getExampleChatId())
                .setText(expected)
        );

        // assert that it is success call
        Assertions.assertTrue(response.isOk());
        Assertions.assertEquals(expected, response.getResult().getText());
    }

    @Test
    public void getYodaStickerSet() {
        // create extended telegram client
        final ExtendedTelegramHttpClient client = extendedClient();

        final TelegramCoreResponse<StickerSet> yodaStickerSet = client.send(new GetStickerSetApi("BabyYoda"));

        // assert that it is success call
        Assertions.assertTrue(yodaStickerSet.isOk());
    }

    @Test
    public void sendStrongYodaLikeStickerByWallE() {
        // create extended telegram client
        final ExtendedTelegramHttpClient client = extendedClient();

        //expected sticker id
        final String expected = "AgADfgIAAladvQo";

        final TelegramCoreResponse<Message> yodaWithLike = client.send(new SendStickerApi()
                .setChatId(BotTest.getExampleChatId())
                .setSticker(() -> "CAACAgIAAxkBAAMRYEPuip1V-qoKxYi1hdmlzAnRmuMAAn4CAAJWnb0KQWJ0X1FsrOQeBA")
        );

        // assert that it is success call
        Assertions.assertTrue(yodaWithLike.isOk());
        Assertions.assertEquals(expected, yodaWithLike.getResult().getSticker().getUniqueId());
    }

}
