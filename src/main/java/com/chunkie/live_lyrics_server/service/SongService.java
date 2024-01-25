package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.mapper.SongMapper;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
public class SongService {

    @Resource
    private SongMapper songMapper;

    @Resource
    private S3Service s3Service;

    @RequestMapping("/uploadSong")
    public boolean uploadSong(@RequestParam(value = "audio") MultipartFile audio,
                              @RequestParam(value = "lyric") MultipartFile lyric,
                              @RequestParam(value = "cover") MultipartFile cover,
                              @RequestBody Song song,
                              HttpServletRequest request){
        String songId = UUID.randomUUID().toString();
        song.setSongId(songId);
        try{
            if (getAudioDuration(audio) != -1) song.setSongDuration(getAudioDuration(audio));
        }catch (Exception e){
            e.printStackTrace();
        }
        // Here to upload file to the S3 Server
        return s3Service.uploadFile("/resources/audio/" + songId, audio) && songMapper.addSong(song) != 0;
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
