package com.chunkie.live_lyrics_server.controller;


import com.chunkie.live_lyrics_server.entity.PlayStatus;
import com.chunkie.live_lyrics_server.service.RoomService;
import com.google.gson.Gson;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class WebSocketController {

    @Resource
    private RoomService roomService;

    @Resource
    private Gson gson;

    @MessageMapping("/playStatus/{roomId}")
    @SendTo("/topic/playStatus/{roomId}")
    public PlayStatus playStatus(@Payload String message, @DestinationVariable String roomId) {
        PlayStatus playStatus = gson.fromJson(message, PlayStatus.class);
        roomService.updatePlayStatus(roomId, playStatus);
        return playStatus;
    }

    @SubscribeMapping("/playStatus/{roomId}")
    public PlayStatus getCurrentStatus(@DestinationVariable String roomId) {
        return roomService.getPlayStatusByRoomId("playStatus_" + roomId);
    }
}
