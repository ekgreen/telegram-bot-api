package com.goodboy.telegram.bot.spring.impl.processors;

import com.goodboy.telegram.bot.spring.api.meta.Infrastructure;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Infrastructure
@Retention(RetentionPolicy.RUNTIME)
@interface OriginFactory {}
