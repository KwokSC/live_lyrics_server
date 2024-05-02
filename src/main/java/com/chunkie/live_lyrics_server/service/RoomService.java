package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.dto.ProgramDTO;
import com.chunkie.live_lyrics_server.dto.ProgrammeDTO;
import com.chunkie.live_lyrics_server.entity.*;
import com.chunkie.live_lyrics_server.exception.UnauthorizedException;
import com.chunkie.live_lyrics_server.mapper.RoomMapper;
import com.chunkie.live_lyrics_server.repo.ProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class RoomService {

    @Resource
    private AuthService authService;

    @Resource
    private SongService songService;

    @Resource
    private UserService userService;

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private S3Service s3Service;

    public Boolean createRoom(Room room, String token) {
        String userId = authService.getUserByToken(token);
        if (userId == null){
            return false;
        }
        room.setRoomId(generateRoomId());
        room.setRoomOwner(userId);
        return roomMapper.createRoom(room) != 0;
    }

    public Boolean updateRoomById(Room room) {
        return roomMapper.updateRoomById(room) != 0;
    }

    public Room getRoomByRoomId(String roomId) {
        return roomMapper.getRoomByRoomId(roomId);
    }

    public Room getRoomByUserAccount(String token) {
        String userId = authService.getUserByToken(token);
        if (userId != null) {
            return roomMapper.getRoomByUserAccount(userId);
        }
        throw new UnauthorizedException();
    }

    private String generateRoomId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(uuid.length() - 6);
    }

}
