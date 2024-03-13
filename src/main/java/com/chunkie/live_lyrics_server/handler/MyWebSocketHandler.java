package com.chunkie.live_lyrics_server.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        // 获取连接 ID
        String sessionId = session.getId();

        // 获取远程地址
        String remoteAddress = session.getRemoteAddress().toString();

        String localAddress = session.getLocalAddress().toString();

        String path = session.getUri().getPath().toString();

        // 获取协议版本
        String protocol = session.getAcceptedProtocol();

        // 打印连接信息
        System.out.println("WebSocket connection established:");
        System.out.println("Session ID: " + sessionId);
        System.out.println("Remote Address: " + remoteAddress);
        System.out.println("Local Address: " + localAddress);
        System.out.println("Path: " + path);
        System.out.println("Protocol: " + protocol);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        // 获取连接 ID
        String sessionId = session.getId();

        // 打印连接关闭信息
        System.out.println("WebSocket connection closed:");
        System.out.println("Session ID: " + sessionId);
    }
}
