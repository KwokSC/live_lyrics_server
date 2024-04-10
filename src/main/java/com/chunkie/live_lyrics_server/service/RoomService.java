package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.dto.ProgramDTO;
import com.chunkie.live_lyrics_server.dto.ProgrammeDTO;
import com.chunkie.live_lyrics_server.entity.*;
import com.chunkie.live_lyrics_server.exception.UnauthorizedException;
import com.chunkie.live_lyrics_server.mapper.RoomMapper;
import com.chunkie.live_lyrics_server.repo.ProgrammeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private ProgrammeRepository programmeRepository;

    @Resource
    private RoomMapper roomMapper;

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

    public ProgrammeDTO getProgrammeByRoomId(String roomId) {
        Programme programme = programmeRepository.findByProgrammeId(roomId);
        ProgrammeDTO programmeDTO = new ProgrammeDTO();
        if (programme == null) {
            Programme newProgramme = new Programme();
            newProgramme.setProgrammeId("programme_" + roomId);
            programmeRepository.save(newProgramme);
            return null;
        }
        programmeDTO.setProgrammeId(programme.getProgrammeId());
        for(Program program : programme.getProgramList()){
            Song song = songService.getSongById(program.getSongId());
            if(song == null){
                continue;
            }
            ProgramDTO programDTO = new ProgramDTO();
            programDTO.setSong(song);
            programDTO.setRecommendations(program.getRecommendations());
            programmeDTO.getProgramList().add(programDTO);
        }
        return programmeDTO;
    }

    private String generateRoomId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(uuid.length() - 6);
    }

}
