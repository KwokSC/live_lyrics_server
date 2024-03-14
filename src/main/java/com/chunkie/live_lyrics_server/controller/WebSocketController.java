package com.chunkie.live_lyrics_server.controller;


import com.chunkie.live_lyrics_server.common.MessageObject;
import com.chunkie.live_lyrics_server.exception.NoTypeMessageException;
import com.chunkie.live_lyrics_server.service.WebsocketService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class WebSocketController {

    @Resource
    private WebsocketService websocketService;

    @MessageMapping("/{roomId}/status.update")
    @SendTo("/topic/{roomId}/public")
    public MessageObject updateStatus(@Payload String message, @DestinationVariable String roomId, SimpMessageHeaderAccessor accessor) {
        // TODO: Handle message logic.
        String messageType = accessor.getFirstNativeHeader("Type");
        if (messageType == null) throw new NoTypeMessageException();
        return websocketService.handleStatusMessage(message, messageType, roomId);
    }

    @MessageMapping("/{roomId}/user.enter")
    @SendTo("/topic/{roomId}/public")
    public MessageObject userEnter(@DestinationVariable String roomId){
        return websocketService.userEnter(roomId);
    }

    @MessageMapping("/{roomId}/user.enter")
    @SendTo("/topic/{roomId}/public")
    public MessageObject userExit(@DestinationVariable String roomId, SimpMessageHeaderAccessor accessor){
        String userId = accessor.getFirstNativeHeader("UserId");
        if (userId == null) {
            userId = accessor.getSessionId();
        }
        return websocketService.userExit(roomId, userId);
    }

    @SubscribeMapping("/{roomId}/public")
    public MessageObject subscribeRoom(@DestinationVariable String roomId, SimpMessageHeaderAccessor accessor) {
        // TODO: Handle subscribe event.
        String userId = accessor.getFirstNativeHeader("UserId");
        if (userId == null) {
            userId = accessor.getSessionId();
        }
        return websocketService.subscribeRoom(roomId, userId);
    }

}
