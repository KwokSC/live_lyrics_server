package com.chunkie.live_lyrics_server.util;

import com.chunkie.live_lyrics_server.service.WebsocketService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

public class LiveSessionDecorator extends WebSocketHandlerDecorator {

    private final WebsocketService websocketService;

    public LiveSessionDecorator(WebSocketHandler delegate, WebsocketService websocketService) {
        super(delegate);
        this.websocketService = websocketService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        websocketService.activateSession(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        super.afterConnectionClosed(session, closeStatus);
        websocketService.deactivateSession(session.getId());
    }
}