package com.chunkie.live_lyrics_server.config;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SquareConfig {

    private static final Logger logger = LoggerFactory.getLogger(SquareConfig.class);

    @Value("${square.accessToken}")
    private String accessToken;

    @Value("${square.environment}")
    private String environment;

    @Bean
    public SquareClient squareClientDev(){
        return new SquareClient.Builder()
                .environment(Environment.fromString(environment))
                .accessToken(accessToken)
                .build();
    }
}
