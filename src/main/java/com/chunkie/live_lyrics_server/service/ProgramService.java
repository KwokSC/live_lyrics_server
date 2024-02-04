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

@Service
public class ProgramService {

    @Resource
    private S3Service s3Service;

    @Resource
    private ProgrammeRepository programmeRepository;

    public Programme addProgramByRoomId(String roomId, Program program){
        Programme programme = programmeRepository.findByProgrammeId(roomId);
        if (programme !=null){
            programme.getProgramList().add(program);
            return programmeRepository.save(programme);
        }
        return null;
    }

    public Boolean uploadAudio(MultipartFile audio){
        return s3Service.uploadFile("/resources/audio/" + audio.getOriginalFilename(), audio);
    }

    public Boolean uploadLyric(MultipartFile lyric){
        return s3Service.uploadFile("/resources/lyric/" + lyric.getOriginalFilename(), lyric);
    }

    private long getAudioDuration(MultipartFile file) {
        try {
            File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(convertedFile);
            AudioFile audioFile = AudioFileIO.read(convertedFile);
            return audioFile.getAudioHeader().getTrackLength();
        } catch (CannotReadException | IOException | TagException | InvalidAudioFrameException | ReadOnlyFileException e) {
            e.printStackTrace();
        }
        return -1; // Indicates failure to get the audio duration
    }
}
