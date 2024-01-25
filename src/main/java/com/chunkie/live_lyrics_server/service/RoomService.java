package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.dto.RoomDTO;
import com.chunkie.live_lyrics_server.entity.PlayStatus;
import com.chunkie.live_lyrics_server.entity.Program;
import com.chunkie.live_lyrics_server.entity.Programme;
import com.chunkie.live_lyrics_server.entity.Room;
import com.chunkie.live_lyrics_server.mapper.RoomMapper;
import com.chunkie.live_lyrics_server.repo.PlayStatusRepository;
import com.chunkie.live_lyrics_server.repo.ProgrammeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoomService {

    @Resource
    private ProgrammeRepository programmeRepository;

    @Resource
    private PlayStatusRepository playStatusRepository;

    @Resource
    private RoomMapper roomMapper;

    public Room getRoomById(String roomId){
        return roomMapper.getRoomById(roomId);
    }

    public Programme getProgrammeByRoomId(String roomId){
       return programmeRepository.findByRoomId(roomId);
    }

    public PlayStatus getPlayStatusByRoomId(String roomId){
        return playStatusRepository.getPlayStatusByRoomId(roomId);
    }

    public RoomDTO newUserEnterRoom(String roomId){
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setPlayStatus(getPlayStatusByRoomId(roomId));
        roomDTO.setProgramme(getProgrammeByRoomId(roomId));
        return roomDTO;
    }

    public Programme addProgramByRoomId(String roomId, Program program){
        Programme programme = programmeRepository.findByRoomId(roomId);
        if (programme !=null){
            programme.getProgramList().add(program);
            return programmeRepository.save(programme);
        }
        return null;
    }
}
