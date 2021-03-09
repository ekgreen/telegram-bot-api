package com.goodboy.telegram.bot.spring.grabber;

import com.goodboy.telegram.bot.api.Update;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Batch {

    private final String uid = UUID.randomUUID().toString();

    private final List<Update> elements;

    public int batchSize() {
        return elements != null ? elements.size() : 0;
    }
}
