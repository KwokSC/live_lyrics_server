package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.dto.ProgramDTO;
import com.chunkie.live_lyrics_server.dto.ProgrammeDTO;
import com.chunkie.live_lyrics_server.entity.*;
import com.chunkie.live_lyrics_server.exception.UnauthorizedException;
import com.chunkie.live_lyrics_server.mapper.RoomMapper;
import com.chunkie.live_lyrics_server.repo.PlayStatusRepository;
import com.chunkie.live_lyrics_server.repo.ProgrammeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    @Resource
    private AuthService authService;

    @Resource
    private SongService songService;

    @Resource
    private ProgrammeRepository programmeRepository;

    @Resource
    private PlayStatusRepository playStatusRepository;

    @Resource
    private RoomMapper roomMapper;

    public Boolean createRoom(Room room) {
        return roomMapper.createRoom(room) != 0;
    }

    public Boolean updateRoomById(Room room) {
        return roomMapper.updateRoomById(room) != 0;
    }

    public Boolean updateStatusById(String roomId, Integer status) {
        Map<String, Object> params = new HashMap<>();
        params.put("roomId", roomId);
        params.put("roomStatus", status);
        return roomMapper.updateStatusById(params) != 0;
    }

    public Room getRoomByRoomId(String roomId) {
        return roomMapper.getRoomByRoomId(roomId);
    }

    public Room getRoomByUserId(String token) {
        String userId = authService.getUserByToken(token);
        if (userId != null) {
            return roomMapper.getRoomByUserId(userId);
        }
        throw new UnauthorizedException();
    }

    public ProgrammeDTO getProgrammeByRoomId(String roomId) {
        Programme programme = programmeRepository.findByProgrammeId(roomId);
        if (programme == null) return null;
        ProgrammeDTO programmeDTO = new ProgrammeDTO();
        programmeDTO.setProgrammeId(programme.getProgrammeId());
        programmeDTO.setProgramList(new ArrayList<>());
        for(Program program : programme.getProgramList()){
            ProgramDTO programDTO = new ProgramDTO();
            Song song = songService.getSongById(program.getSongId());
            programDTO.setSong(song);
            programDTO.setRecommendations(program.getRecommendations());
            programmeDTO.getProgramList().add(programDTO);
        }
        return programmeDTO;
    }

    public PlayStatus getPlayStatusByRoomId(String roomId) {
        return playStatusRepository.getPlayStatusByPlayStatusId(roomId);
    }

    public void updatePlayStatus(PlayStatus playStatus) {
        playStatusRepository.save(playStatus);
    }

}
