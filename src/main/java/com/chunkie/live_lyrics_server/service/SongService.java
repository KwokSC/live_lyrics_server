package com.chunkie.live_lyrics_server.service;


import com.chunkie.live_lyrics_server.dto.LyricDTO;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.mapper.SongMapper;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class SongService {

    @Resource
    private SongMapper songMapper;

    @Resource
    private S3Service s3Service;

    private final String ALBUM_IMAGE_PREFIX = "resources/images/album/";
    private final String LRC_PREFIX = "resources/lyric/";
    private final String AUDIO_PREFIX = "resources/audio/";

    private final static Logger logger = LoggerFactory.getLogger(SongService.class);

    public Boolean uploadSong(Song song) {
        return songMapper.addSong(song) != 0;
    }

    public Boolean uploadAlbumCover(MultipartFile image) {
        return s3Service.uploadFile(ALBUM_IMAGE_PREFIX + image.getOriginalFilename(), image);
    }

    public Boolean uploadAudio(MultipartFile audio) {
        return s3Service.uploadFile(AUDIO_PREFIX + audio.getOriginalFilename(), audio);
    }

    public Boolean uploadLyric(List<MultipartFile> lyric) {
        for (MultipartFile file : lyric) {
            String filename = file.getOriginalFilename();
            filename = filename.substring(0, filename.lastIndexOf("."));
            String id = filename.substring(filename.lastIndexOf("_") + 1);
            if (!s3Service.uploadFile(LRC_PREFIX + id + "/" + file.getOriginalFilename(), file))
                return false;
        }
        return true;
    }

    public String getAlbumCoverById(String songId) {
        String id = songId.substring(songId.lastIndexOf("_") + 1);
        return s3Service.getFile(ALBUM_IMAGE_PREFIX + "album_" + id);
    }

    public String getAudioById(String songId) {
        String id = songId.substring(songId.lastIndexOf("_") + 1);
        return s3Service.getFile(AUDIO_PREFIX + "audio_" + id);
    }

    public List<LyricDTO> getLyricsById(String songId) {
        return s3Service.getLyricsById(LRC_PREFIX + songId.substring(songId.indexOf("_") + 1) + "/");
    }

    public Song getSongById(String songId) {
        return songMapper.getSongById(songId);
    }

    private long getAudioDuration(MultipartFile file) {
        try {
            File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(convertedFile);
            AudioFile audioFile = AudioFileIO.read(convertedFile);
            return audioFile.getAudioHeader().getTrackLength();
        } catch (CannotReadException | IOException | TagException | InvalidAudioFrameException | ReadOnlyFileException e) {
            logger.error(e.getMessage());
        }
        return -1; // Indicates failure to get the audio duration
    }

}
