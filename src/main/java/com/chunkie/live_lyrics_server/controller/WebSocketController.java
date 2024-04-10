package com.chunkie.live_lyrics_server.controller;


import com.chunkie.live_lyrics_server.common.MessageObject;
import com.chunkie.live_lyrics_server.exception.NoTypeMessageException;
import com.chunkie.live_lyrics_server.service.WebsocketService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class WebSocketController {

    @Resource
    private WebsocketService websocketService;

    @MessageMapping("/{roomId}/status.update")
    @SendTo("/topic/{roomId}/public")
    public MessageObject updateStatus(@Payload String message, @DestinationVariable String roomId, SimpMessageHeaderAccessor accessor) {
        String messageType = accessor.getFirstNativeHeader("Type");
        if (messageType == null) throw new NoTypeMessageException();
        return websocketService.handleStatusMessage(message, messageType, roomId);
    }

    @MessageMapping("/{roomId}/user.enter")
    @SendTo("/topic/{roomId}/public")
    public MessageObject userEnter(@DestinationVariable String roomId, SimpMessageHeaderAccessor accessor){
        String userId = accessor.getFirstNativeHeader("UserId");
        return websocketService.userEnter(roomId, userId);
    }

    @MessageMapping("/{roomId}/user.exit")
    @SendTo("/topic/{roomId}/public")
    public MessageObject userExit(@DestinationVariable String roomId, SimpMessageHeaderAccessor accessor){
        String userId = accessor.getFirstNativeHeader("UserId");
        return websocketService.userExit(roomId, userId);
    }

    @MessageMapping("/{roomId}/chat")
    @SendTo("/topic/{roomId}/public")
    public MessageObject chat(@DestinationVariable String roomId, @Payload String message){
        return websocketService.handleChatMessage(message, roomId);
    }

    @SubscribeMapping("/{roomId}/public")
    public MessageObject subscribeRoom(@DestinationVariable String roomId) {
        return websocketService.subscribeRoom(roomId);
    }

}
