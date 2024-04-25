package com.chunkie.live_lyrics_server.config;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.authentication.BearerAuthModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SquareConfig {

    @Value("${square.accessToken}")
    private String accessToken;

    @Value("${square.environment}")
    private String environment;

    @Bean
    public SquareClient squareClientDev(){
        return new SquareClient.Builder()
                .environment(Environment.fromString(environment))
                .bearerAuthCredentials(new BearerAuthModel.Builder(accessToken).build())
                .userAgentDetail("busk-live-spring")
                .build();
    }
}
