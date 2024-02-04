package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.Constants;
import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.dto.SongDTO;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.service.SongService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
public class SongController {

    @Resource
    private SongService songService;

    @RequestMapping("/song/getSongById")
    public ResponseObject getSongById(@RequestParam String songId){
        return new ResponseObject(songService.getSongInfo(songId), Constants.Code.NORMAL, Constants.Msg.SUCCESS);
    }

    @RequestMapping("/song/uploadSongInfo")
    public ResponseObject uploadSongInfo(@RequestBody Song song){
        return new ResponseObject(songService.uploadSongInfo(song), Constants.Code.NORMAL, Constants.Msg.SUCCESS);
    }
}
