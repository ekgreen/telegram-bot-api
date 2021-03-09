package com.goodboy.telegram.bot.spring.api.actions;

import com.goodboy.telegram.bot.api.methods.Api;
import com.goodboy.telegram.bot.api.methods.action.Action;
import org.jetbrains.annotations.NotNull;

public interface FastAction<T extends Api> {

    T heavyweight();

    default @NotNull Action fastAction(){
        return Action.TYPING;
    }
}
