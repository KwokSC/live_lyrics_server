package com.chunkie.live_lyrics_server.config;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.authentication.BearerAuthModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SquareConfig {

    private static final Logger logger = LoggerFactory.getLogger(SquareConfig.class);

    @Value("${square.accessToken}")
    private String accessToken;

    @Bean
    @Profile("dev")
    public SquareClient squareClientDev(){
        return new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .bearerAuthCredentials(new BearerAuthModel.Builder(accessToken).build())
                .build();
    }

    @Bean
    @Profile("prod")
    public SquareClient squareClientProd(){
        return new SquareClient.Builder()
                .environment(Environment.PRODUCTION)
                .bearerAuthCredentials(new BearerAuthModel.Builder(accessToken).build())
                .build();
    }
}
