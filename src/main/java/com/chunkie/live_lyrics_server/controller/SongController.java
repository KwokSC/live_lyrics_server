package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.service.SongService;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
public class SongController {

    @Resource
    private SongService songService;

}
