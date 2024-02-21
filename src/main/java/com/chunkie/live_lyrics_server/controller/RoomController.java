package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.annotation.LoginRequired;
import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.dto.ProgrammeDTO;
import com.chunkie.live_lyrics_server.entity.Room;
import com.chunkie.live_lyrics_server.service.RoomService;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping("/getHotRoom")
    public ResponseObject getHotRoom(){
        return new ResponseObject();
    }

    @RequestMapping("/createRoom")
    public ResponseObject createRoom(@RequestBody Room room) {
        if (roomService.createRoom(room)) {
            return ResponseObject.success(null, "Successfully create a room.");
        }
        return ResponseObject.fail(null, "Fail to create a room.");
    }

    @RequestMapping("/getRoomByRoomId")
    public ResponseObject getRoomByRoomId(@RequestParam String roomId) {
        Room room = roomService.getRoomByRoomId(roomId);
        return room != null ? ResponseObject.success(room, "Find the room.") : ResponseObject.fail(null, "Room not found.");
    }

    @RequestMapping("/getProgrammeById")
    public ResponseObject getProgrammeById(@RequestParam String roomId) {
        ProgrammeDTO programme = roomService.getProgrammeByRoomId("programme_" + roomId);
        return ResponseObject.success(programme, "Find the programme for the room.");
    }

    @LoginRequired
    @RequestMapping("/getRoomByUserId")
    public ResponseObject getRoomByUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Room room = roomService.getRoomByUserId(token);
        return room != null ? ResponseObject.success(room, "Find the room.") : ResponseObject.fail(null, "Room not found.");
    }

    @RequestMapping("/getPlayStatusById")
    public ResponseObject getPlayStatusById(@RequestParam String roomId){
        return ResponseObject.success(roomService.getPlayStatusByRoomId("playStatus_"+roomId), "Room " + roomId + " play status found.");
    }
}