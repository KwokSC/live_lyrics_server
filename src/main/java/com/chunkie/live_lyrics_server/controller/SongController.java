package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.service.SongService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class SongController {

    @Resource
    private SongService songService;

    @RequestMapping("/song/getSongById")
    public ResponseObject getSongById(@RequestParam String songId) {
        return ResponseObject.success(songService.getSongById(songId), "Get song details.");
    }

    @RequestMapping("/song/getAlbumCoverById")
    public ResponseObject getAlbumCoverById(@RequestParam String songId) {
        return ResponseObject.success(songService.getAlbumCoverById(songId), "Get album cover image.");
    }

    @RequestMapping("/song/getLyricsById")
    public ResponseObject getLyricsById(@RequestParam String songId) {
        return ResponseObject.success(songService.getLyricsById(songId), "Find lyrics of the song.");
    }

    @RequestMapping("/song/getAudioById")
    public ResponseObject getAudioById(@RequestParam String songId) {
        return ResponseObject.success(songService.getAudioById(songId), "Find audio of the song.");
    }

    @RequestMapping("/song/deleteSong")
    public ResponseObject deleteSong(@RequestParam String roomId, @RequestParam String songId) {
        songService.deleteSong(roomId, songId);
        return ResponseObject.success(null, "Delete song details.");
    }

    @RequestMapping("/song/uploadSong")
    public ResponseObject uploadSong(@RequestBody Song song) {
        return songService.uploadSong(song) ? ResponseObject.success(null, "Upload successfully.") :
                ResponseObject.fail(null, "Upload unsuccessfully.");
    }

    @RequestMapping("/song/uploadAudio")
    public ResponseObject uploadAudio(@RequestParam(value = "audio") MultipartFile audio) {
        return songService.uploadAudio(audio) ? ResponseObject.success(null, "Upload successfully.") :
                ResponseObject.fail(null, "Upload unsuccessfully.");
    }

    @RequestMapping("/song/uploadAlbumCover")
    public ResponseObject uploadAlbumCover(@RequestParam(value = "album") MultipartFile album) {
        return songService.uploadAlbumCover(album) ? ResponseObject.success(null, "Upload successfully.") :
                ResponseObject.fail(null, "Fail to upload album cover.");
    }

//    @RequestMapping("/song/uploadRecommendationImg")
//    public ResponseObject uploadRecommendationImg(@RequestParam(value = "image") List<MultipartFile> images){
//
//    }

    @RequestMapping("/song/uploadLyric")
    public ResponseObject uploadLyric(@RequestParam(value = "lyric") List<MultipartFile> lyric) {
        return songService.uploadLyric(lyric) ? ResponseObject.success(null, "Upload successfully.") : ResponseObject.fail(null, "Fail to upload lyric.");
    }
}
