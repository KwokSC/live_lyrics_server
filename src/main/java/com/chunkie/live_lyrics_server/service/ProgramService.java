package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.entity.Program;
import com.chunkie.live_lyrics_server.entity.Programme;
import com.chunkie.live_lyrics_server.repo.ProgrammeRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    @Resource
    private ProgrammeRepository programmeRepository;

    public boolean addProgramByRoomId(String roomId, Program program) {
        Programme programme = programmeRepository.findByProgrammeId(roomId);
        if (programme == null) {
            Programme newProgramme = new Programme(roomId, new ArrayList<>());
            newProgramme.getProgramList().add(program);
            programmeRepository.save(newProgramme);
            return true;
        }
        programme.getProgramList().add(program);
        programmeRepository.save(programme);
        return true;
    }

    public boolean deleteProgramById(String roomId, String songId) {
        Programme programme = programmeRepository.findByProgrammeId(roomId);
        if (programme != null) {
            List<Program> programList =
                    programme.getProgramList().stream().filter(program -> !program.getSongId().equals(songId)).collect(Collectors.toList());
            programme.setProgramList(programList);
            programmeRepository.save(programme);
            return true;
        }
        return false;
    }
}
