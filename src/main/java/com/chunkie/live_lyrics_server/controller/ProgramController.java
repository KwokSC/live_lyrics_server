package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.entity.Program;
import com.chunkie.live_lyrics_server.service.ProgramService;
import com.chunkie.live_lyrics_server.service.SongService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class ProgramController {

    @Resource
    private ProgramService programService;

    @Resource
    private SongService songService;

    @RequestMapping("/program/uploadAudio")
    public ResponseObject uploadAudio(MultipartFile audio){
        return new ResponseObject();
    }

    @RequestMapping("/program/uploadProgram")
    public ResponseObject uploadProgram(@RequestBody Program program){
        return new ResponseObject();
    }

    @RequestMapping("/program/deleteProgram")
    public ResponseObject deleteProgram(){
        return new ResponseObject();
    }
}
