package com.chunkie.live_lyrics_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ExecutorConfig {

    private final int THREAD_AMOUNT = 10;

    @Bean
    public ScheduledExecutorService scheduledExecutorService(){
        return Executors.newScheduledThreadPool(THREAD_AMOUNT);
    }
}
