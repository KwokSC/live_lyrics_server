package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.service.SongService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/song")
@RestController
public class SongController {

    @Resource
    private SongService songService;


    @RequestMapping("/getSongById")
    public ResponseObject getSongById(@RequestParam String songId) {
        return ResponseObject.success(songService.getSongById(songId), "Get song details.");
    }

    @RequestMapping("/uploadSong")
    public ResponseObject uploadSong(@RequestBody Song song) {
        return songService.uploadSong(song) ? ResponseObject.success(null, "Upload successfully.") :
                ResponseObject.fail(null, "Upload unsuccessfully.");
    }

}
