package com.chunkie.live_lyrics_server.handler;

import com.chunkie.live_lyrics_server.service.WebsocketService;
import com.chunkie.live_lyrics_server.util.LiveSessionDecorator;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

public class LiveSessionHandler implements WebSocketHandlerDecoratorFactory {

    private final WebsocketService websocketService;

    public LiveSessionHandler(WebsocketService websocketService) {
        this.websocketService = websocketService;
    }

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new LiveSessionDecorator(handler, websocketService);
    }
}