package com.goodboy.telegram.bot.spring.api.actions;

public interface Solo extends FastAction<VoidApi> {

    void solo();

    default VoidApi heavyweight(){
        solo();
        return null;
    }
}
