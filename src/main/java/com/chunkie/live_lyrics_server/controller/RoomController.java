package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.Constants;
import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.Room;
import com.chunkie.live_lyrics_server.service.RoomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Resource
    private RoomService roomService;

    @RequestMapping("/getRoomById")
    public ResponseObject getRoomStatus(@RequestParam String roomId){
        Room room = roomService.getRoomById(roomId);
        return new ResponseObject(room, Constants.Code.NORMAL, Constants.Msg.SUCCESS);
    }
}