package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.entity.Program;
import com.chunkie.live_lyrics_server.entity.Programme;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.repo.ProgrammeRepository;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    @Resource
    private S3Service s3Service;

    @Resource
    private ProgrammeRepository programmeRepository;

    public Boolean addProgramByRoomId(String roomId, Program program) {
        Programme programme = programmeRepository.findByProgrammeId("programme_" + roomId);
        if (programme != null) {
            programme.getProgramList().add(program);
            programmeRepository.save(programme);
            return true;
        }
        return false;
    }

    public Boolean deleteProgramById(String roomId, String songId) {
        Programme programme = programmeRepository.findByProgrammeId("programme_" + roomId);
        if (programme != null) {
            programme.getProgramList().stream().filter(program -> !program.getSongId().equals(songId)).collect(Collectors.toList());
            return true;
        }
        return false;
    }
}
