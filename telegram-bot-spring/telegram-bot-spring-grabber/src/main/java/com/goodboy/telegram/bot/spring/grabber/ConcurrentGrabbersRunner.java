package com.goodboy.telegram.bot.spring.grabber;

import org.springframework.beans.factory.DisposableBean;

public interface ConcurrentGrabbersRunner extends GrabbersRunner, DisposableBean {}
