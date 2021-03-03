package com.goodboy.telegram.example.service;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
public class HiBrownieCleaningService implements BrownieCleaningService{

    private final static String[] LOCATIONS = {
            "kitchen", "bathroom", "living room", "bedroom"
    };

    public @Nonnull String sayHi(String sender, String message){
        final String place = LOCATIONS[(int) (4 * Math.random())];

        return String.format("Hi %s, im really enjoy to answer u on message [%s], but now I'm cleaning %s", sender, message, place);
    }
}
