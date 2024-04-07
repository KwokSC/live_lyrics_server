package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.annotation.LoginRequired;
import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.PlayerStatus;
import com.chunkie.live_lyrics_server.dto.ProgrammeDTO;
import com.chunkie.live_lyrics_server.entity.RoomStatus;
import com.chunkie.live_lyrics_server.entity.Room;
import com.chunkie.live_lyrics_server.service.LiveService;
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

    @Resource
    private LiveService liveService;

    @RequestMapping("/getHotRooms")
    public ResponseObject getHotRooms() {
        return new ResponseObject();
    }

    @LoginRequired
    @RequestMapping("/createRoom")
    public ResponseObject createRoom(@RequestBody Room room, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (roomService.createRoom(room, token)) {
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

    @RequestMapping("/getRoomByUserId")
    @LoginRequired
    public ResponseObject getRoomByUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Room room = roomService.getRoomByUserId(token);
        return room != null ? ResponseObject.success(room, "Find the room.") : ResponseObject.fail(null, "Room not found.");
    }

    @RequestMapping("/getPlayStatusById")
    public ResponseObject getPlayStatusById(@RequestParam String roomId) {
        PlayerStatus playStatus = liveService.getPlayerStatusByRoomId(roomId);
        return playStatus != null ? ResponseObject.success(playStatus, "Room " + roomId + " play status found.") : ResponseObject.fail(null, "Room is not online.");
    }

    @RequestMapping("/getRoomStatusById")
    public ResponseObject getRoomStatusById(@RequestParam String roomId){
        RoomStatus roomStatus = liveService.getRoomStatusByRoomId(roomId);
        return roomStatus != null ? ResponseObject.success(roomStatus, "Room " + roomId + " play status found.") : ResponseObject.fail(null, "Room is not online.");
    }

    @RequestMapping("/startLive")
    @LoginRequired
    public ResponseObject startLive(@RequestParam String roomId) {
        return liveService.startLive(roomId) ? ResponseObject.success(null, "Room " + roomId + " start live.") : ResponseObject.fail(null, "Fail to start live.");
    }

    @RequestMapping("/endLive")
    public ResponseObject endLive(@RequestParam String roomId) {
        liveService.endLive(roomId);
        return ResponseObject.success(null, "Room " + roomId + " end live.");
    }
}