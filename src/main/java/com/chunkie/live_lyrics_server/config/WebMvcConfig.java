package com.chunkie.live_lyrics_server.config;

import com.chunkie.live_lyrics_server.interceptor.AuthInterceptor;
import com.chunkie.live_lyrics_server.interceptor.IdempotentInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor authInterceptor(){
        return new AuthInterceptor();
    }

    @Bean
    public IdempotentInterceptor idempotentInterceptor(){
        return new IdempotentInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedHeaders("*");
    }
}
