package com.chunkie.live_lyrics_server.interceptor;

import com.chunkie.live_lyrics_server.service.WebsocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiveChannelInterceptor implements ChannelInterceptor {

    private final WebsocketService websocketService;

    private static final Logger logger = LoggerFactory.getLogger(LiveChannelInterceptor.class);

    public LiveChannelInterceptor(WebsocketService websocketService) {
        logger.info(String.valueOf(websocketService == null));
        this.websocketService = websocketService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String userId = accessor.getFirstNativeHeader("UserId");
        String sessionId = accessor.getSessionId();

//        String destination = accessor.getDestination();
//        Matcher matcher = null;
//        if (destination != null) {
//            matcher = topicPattern.matcher(destination);
//        }
//        StompCommand command = accessor.getCommand();
//        SimpMessageType type = null;
//        if (command != null) {
//            type = command.getMessageType();
//        }
//        if (type != null) {
//            switch (type) {
//                case SUBSCRIBE:
//                case CONNECT:
//                    if (userId != null)
//                        websocketService.activateSession(userId, sessionId);
//                    break;
//            }
//        }

        return message;
    }
}
