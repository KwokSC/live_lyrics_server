package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.common.ResponseObject;
import com.chunkie.live_lyrics_server.dto.ProgrammeDTO;
import com.chunkie.live_lyrics_server.entity.Program;
import com.chunkie.live_lyrics_server.service.ProgramService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/program")
@RestController
public class ProgramController {

    @Resource
    private ProgramService programService;


    @RequestMapping("/getAlbumCoverById")
    public ResponseObject getAlbumCoverById(@RequestParam String songId) {
        return ResponseObject.success(programService.getAlbumCoverById(songId), "Get album cover image.");
    }

    @RequestMapping("/getLyricsById")
    public ResponseObject getLyricsById(@RequestParam String songId) {
        return ResponseObject.success(programService.getLyricsById(songId), "Find lyrics of the song.");
    }

    @RequestMapping("/getAudioById")
    public ResponseObject getAudioById(@RequestParam String songId) {
        return ResponseObject.success(programService.getAudioById(songId), "Find audio of the song.");
    }

    @RequestMapping("/uploadAudio")
    public ResponseObject uploadAudio(@RequestParam(value = "audio") MultipartFile audio) {
        return programService.uploadAudio(audio) ? ResponseObject.success(null, "Upload successfully.") :
                ResponseObject.fail(null, "Upload unsuccessfully.");
    }

    @RequestMapping("/uploadAlbumCover")
    public ResponseObject uploadAlbumCover(@RequestParam(value = "album") MultipartFile album) {
        return programService.uploadAlbumCover(album) ? ResponseObject.success(null, "Upload successfully.") :
                ResponseObject.fail(null, "Fail to upload album cover.");
    }

//    @RequestMapping("/song/uploadRecommendationImg")
//    public ResponseObject uploadRecommendationImg(@RequestParam(value = "image") List<MultipartFile> images){
//
//    }

    @RequestMapping("/uploadLyric")
    public ResponseObject uploadLyric(@RequestParam(value = "lyric") List<MultipartFile> lyric) {
        return programService.uploadLyric(lyric) ? ResponseObject.success(null, "Upload successfully.") :
                ResponseObject.fail(null, "Fail to upload lyric.");
    }

    @RequestMapping("/getProgrammeById")
    public ResponseObject getProgrammeById(@RequestParam String roomId) {
        ProgrammeDTO programme = programService.getProgrammeByRoomId(roomId);
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
