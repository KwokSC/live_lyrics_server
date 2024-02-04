package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.annotation.LoginRequired;
import com.chunkie.live_lyrics_server.common.Constants;
import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.Programme;
import com.chunkie.live_lyrics_server.entity.Room;
import com.chunkie.live_lyrics_server.service.RoomService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Resource
    private RoomService roomService;

    @RequestMapping("/getRoomByRoomId")
    public ResponseObject getRoomByRoomId(@RequestParam String roomId){
        Room room = roomService.getRoomByRoomId(roomId);
        return new ResponseObject(room, Constants.Code.NORMAL, Constants.Msg.SUCCESS);
    }

    @RequestMapping("/getProgrammeById")
    public ResponseObject getProgrammeById(@RequestParam String roomId){
        Programme programme = roomService.getProgrammeByRoomId("programme_" + roomId);
        return new ResponseObject(programme, Constants.Code.NORMAL, Constants.Msg.SUCCESS);
    }

    @LoginRequired
    @RequestMapping("/getRoomByUserId")
    public ResponseObject getRoomByUserId(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Room room = roomService.getRoomByUserId(token);
        return new ResponseObject(room, Constants.Code.NORMAL, Constants.Msg.SUCCESS);
    }
}