package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.entity.PlayStatus;
import com.chunkie.live_lyrics_server.entity.Program;
import com.chunkie.live_lyrics_server.entity.Programme;
import com.chunkie.live_lyrics_server.entity.Room;
import com.chunkie.live_lyrics_server.exception.UnauthorizedException;
import com.chunkie.live_lyrics_server.mapper.RoomMapper;
import com.chunkie.live_lyrics_server.repo.PlayStatusRepository;
import com.chunkie.live_lyrics_server.repo.ProgrammeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoomService {

    @Resource
    private AuthService authService;

    @Resource
    private ProgrammeRepository programmeRepository;

    @Resource
    private PlayStatusRepository playStatusRepository;

    @Resource
    private RoomMapper roomMapper;

    public Boolean createRoom(Room room){
        return roomMapper.createRoom(room) != 0;
    }

    public Room getRoomByRoomId(String roomId){
        return roomMapper.getRoomByRoomId(roomId);
    }

    public Room getRoomByUserId(String token){
        String userId = authService.getUserByToken(token);
        if (userId != null){
            return roomMapper.getRoomByUserId(userId);
        }
        throw new UnauthorizedException();
    }

    public Programme getProgrammeByRoomId(String roomId){
       return programmeRepository.findByProgrammeId(roomId);
    }

    public PlayStatus getPlayStatusByRoomId(String roomId){
        return playStatusRepository.getPlayStatusByPlayStatusId(roomId);
    }

    public void updatePlayStatus(PlayStatus playStatus){
        playStatusRepository.save(playStatus);
    }

}
