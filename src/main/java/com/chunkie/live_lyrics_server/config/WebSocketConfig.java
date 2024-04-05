package com.chunkie.live_lyrics_server.config;

import com.chunkie.live_lyrics_server.handler.LiveSessionHandler;
import com.chunkie.live_lyrics_server.interceptor.LiveChannelInterceptor;
import com.chunkie.live_lyrics_server.service.WebsocketService;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import javax.annotation.Resource;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Resource
    private WebsocketService websocketService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app", "/topic");
        registry.setUserDestinationPrefix("/user");
    }
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new LiveChannelInterceptor(websocketService));
//    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(new LiveSessionHandler(websocketService));
    }
}