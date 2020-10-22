package com.goodboy.telegram.bot.api.client.adapter;

import com.goodboy.telegram.bot.api.client.Request;
import com.goodboy.telegram.bot.api.request.Uploading;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface MultipartHttpClientHandler {

   <V> Iterable<Part<?>> parts(Request<V> request);

    Predicate<Request<?>> condition();

    @Data
    @AllArgsConstructor
    class Part<V>{

        private final String key;
        private final V handler;

    }

    @Getter
    class StreamPart extends Part<Supplier<byte[]>>{
        private final String fileName;

        public StreamPart(String key, String fileName, Supplier<byte[]> handler) {
            super(key, handler);
            this.fileName = fileName;
        }
    }

    class StringPart extends Part<String>{
        public StringPart(String key, String handler) {
            super(key, handler);
        }
    }
}
