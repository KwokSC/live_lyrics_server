package com.chunkie.live_lyrics_server.service;


import com.chunkie.live_lyrics_server.dto.LyricDTO;
import com.chunkie.live_lyrics_server.entity.Program;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.mapper.SongMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SongService {

    @Resource
    private SongMapper songMapper;

    @Resource
    private ProgramService programService;


    @Resource
    private S3Service s3Service;

    private final String ALBUM_IMAGE_PREFIX = "resources/images/album/";
    private final String LRC_PREFIX = "resources/lyric/";
    private final String AUDIO_PREFIX = "resources/audio/";

    private final static Logger logger = LoggerFactory.getLogger(SongService.class);

    public boolean uploadSong(Song song) {
        return songMapper.addSong(song) != 0;
    }

    public boolean submit(String roomId, String songJson, String programJson, MultipartFile audio, MultipartFile image,
                          List<MultipartFile> lyrics) {
        Gson gson = new Gson();
        Song song = gson.fromJson(songJson, Song.class);
        Program program = gson.fromJson(programJson, Program.class);
        try {
            songMapper.addSong(song);
            programService.addProgramByRoomId(roomId, program);
            uploadAudio(audio);
            uploadAlbumCover(image);
            uploadLyric(lyrics);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            s3Service.deleteFile(AUDIO_PREFIX + audio.getOriginalFilename());
            s3Service.deleteFile(ALBUM_IMAGE_PREFIX + image.getOriginalFilename());
            String filename = lyrics.get(0).getOriginalFilename();
            assert filename != null;
            String id = filename.substring(filename.lastIndexOf("_") + 1);
            s3Service.deleteFile(LRC_PREFIX + id);
            songMapper.deleteSongById(song.getSongId());
            programService.deleteProgramById(roomId, program.getSongId());
            return false;
        }
    }

    public boolean uploadAudio(MultipartFile audio) {
        return s3Service.uploadFile(AUDIO_PREFIX + audio.getOriginalFilename(), audio);
    }

    public boolean uploadAlbumCover(MultipartFile image) {
        return s3Service.uploadFile(ALBUM_IMAGE_PREFIX + image.getOriginalFilename(), image);
    }

    public boolean uploadLyric(List<MultipartFile> lyric) {
        for (MultipartFile file : lyric) {
            String filename = file.getOriginalFilename();
            assert filename != null;
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
}
