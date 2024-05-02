package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.dto.ProgrammeDTO;
import com.chunkie.live_lyrics_server.entity.Program;
import com.chunkie.live_lyrics_server.service.ProgramService;
import com.chunkie.live_lyrics_server.service.SongService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/program")
@RestController
public class ProgramController {

    @Resource
    private ProgramService programService;

    @Resource
    private SongService songService;

    @RequestMapping("/getProgrammeById")
    public ResponseObject getProgrammeById(@RequestParam String roomId) {
        ProgrammeDTO programme = songService.getProgrammeByRoomId(roomId);
        return ResponseObject.success(programme, "Find the programme for the room.");
    }

    @RequestMapping("/uploadProgram")
    public ResponseObject uploadProgram(@RequestBody Program program, @RequestParam String roomId) {
        return programService.addProgramByRoomId(roomId, program) ? ResponseObject.success(null, "Successfully update programme") : ResponseObject.fail(null, "Fail to upload program.");
    }

    @RequestMapping("/deleteProgram")
    public ResponseObject deleteProgram(@RequestParam String roomId, @RequestParam String songId) {
        return programService.deleteProgramById(roomId, songId) ? ResponseObject.success(null, "Successfully delete program") : ResponseObject.fail(null, "Fail to delete program.");
    }
}
