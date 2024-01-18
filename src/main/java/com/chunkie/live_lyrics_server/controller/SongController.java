package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.Constants;
import com.chunkie.live_lyrics_server.common.ResponseObj;
import com.chunkie.live_lyrics_server.service.SongService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SongController {

    @Resource
    private SongService songService;

    @MessageMapping("/playStatus/{roomId}")
    @SendTo("/topic/playStatus/{roomId}")
    public ResponseObj playStatus(@Payload String message, @DestinationVariable String roomId){
        ResponseObj responseObj = new ResponseObj();
        responseObj.setCode(Constants.Code.NORMAL);
        responseObj.setData(message);
        responseObj.setMsg(Constants.Msg.SUCCESS);
        return responseObj;
    }

    @SubscribeMapping("/playStatus/{roomId}")
    public ResponseObj getCurrentStatus(@DestinationVariable String roomId){
        System.out.println("New Client subscribed.");
        return new ResponseObj("entered room" + roomId, Constants.Code.NORMAL, Constants.Msg.SUCCESS);
    }
}
