package com.chunkie.live_lyrics_server.config;

import com.chunkie.live_lyrics_server.common.Constants;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.authentication.BearerAuthModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class SquareConfig {

    private static final Logger logger = LoggerFactory.getLogger(SquareConfig.class);

    @Bean
    public SquareClient squareClient(){

        InputStream inputStream = SquareConfig.class.getResourceAsStream("/config.properties");
        Properties prop = new Properties();

        try {
            prop.load(inputStream);
        } catch (IOException e) {
            logger.error("Error reading properties file");
        }

        return new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .bearerAuthCredentials(new BearerAuthModel.Builder(prop.getProperty("SQUARE_ACCESS_TOKEN")).build())
                .build();
    }
}
