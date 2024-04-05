package com.chunkie.live_lyrics_server.config;

import com.chunkie.live_lyrics_server.common.Constants;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.authentication.BearerAuthModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SquareConfig {

    @Bean
    public SquareClient squareClient(){
        return new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .bearerAuthCredentials(new BearerAuthModel.Builder(Constants.SQUARE.TOKEN).build())
                .build();
    }
}
