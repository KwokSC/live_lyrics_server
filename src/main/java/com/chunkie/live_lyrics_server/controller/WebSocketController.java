package com.chunkie.live_lyrics_server.controller;


import com.chunkie.live_lyrics_server.common.Constants;
import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.dto.RoomDTO;
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
    public ResponseObject playStatus(@Payload String message, @DestinationVariable String roomId){
        ResponseObject responseObject = new ResponseObject();
        PlayStatus playStatus = gson.fromJson(message, PlayStatus.class);
        playStatus.setPlayStatusId("playStatus_" + roomId);
        roomService.updatePlayStatus(playStatus);
        responseObject.setData(playStatus);
        responseObject.setCode(Constants.Code.NORMAL);
        responseObject.setMsg(Constants.Msg.SUCCESS);
        return responseObject;
    }

    @SubscribeMapping("/playStatus/{roomId}")
    public ResponseObject getCurrentStatus(@DestinationVariable String roomId){
        PlayStatus playStatus = roomService.getPlayStatusByRoomId("playStatus_"+roomId);
        return new ResponseObject(playStatus, Constants.Code.NORMAL, Constants.Msg.SUCCESS);
    }
}
