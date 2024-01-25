package com.chunkie.live_lyrics_server.controller;


import com.chunkie.live_lyrics_server.common.Constants;
import com.chunkie.live_lyrics_server.common.ResponseObject;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    @MessageMapping("/playStatus/{roomId}")
    @SendTo("/topic/playStatus/{roomId}")
    public ResponseObject playStatus(@Payload String message, @DestinationVariable String roomId){
        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(Constants.Code.NORMAL);
        responseObject.setData(message);
        responseObject.setMsg(Constants.Msg.SUCCESS);
        return responseObject;
    }

    @SubscribeMapping("/playStatus/{roomId}")
    public ResponseObject getCurrentStatus(@DestinationVariable String roomId){
        System.out.println("New Client subscribed.");
        return new ResponseObject("entered room" + roomId, Constants.Code.NORMAL, Constants.Msg.SUCCESS);
    }
}
