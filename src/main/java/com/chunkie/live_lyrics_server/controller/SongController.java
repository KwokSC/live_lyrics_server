package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.service.SongService;
import org.springframework.http.HttpStatus;
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

    @RequestMapping("/song/uploadSongInfo")
    public ResponseObject uploadSong(@RequestBody Song song) {
        return songService.uploadSong(song) ? ResponseObject.success(null, "Upload successfully.") : ResponseObject.fail(null, "Upload unsuccessfully.");
    }

    @RequestMapping("/song/uploadAudio")
    public ResponseObject uploadAudio(@RequestParam MultipartFile audio) {
        return new ResponseObject();
    }

    @RequestMapping("/song/uploadAlbumCover")
    public ResponseObject uploadAlbumCover(@RequestParam MultipartFile image) {
        return new ResponseObject();
    }

    @RequestMapping("/song/uploadLyric")
    public ResponseObject uploadLyric(@RequestParam List<MultipartFile> lyric) {
        return new ResponseObject();
    }
}
