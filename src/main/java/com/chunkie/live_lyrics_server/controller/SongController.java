package com.chunkie.live_lyrics_server.controller;

import com.chunkie.live_lyrics_server.annotation.LoginRequired;
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

    @RequestMapping("/song/getLyricsById")
    public ResponseObject getLyricsById(@RequestParam String songId) {
        return ResponseObject.success(songService.getLyricsById(songId), "Find lyrics of the song.");
    }

    @RequestMapping("/song/getAudioById")
    public ResponseObject getAudioById(@RequestParam String songId) {
        return ResponseObject.success(songService.getAudioById(songId), "Find audio of the song.");
    }

    @RequestMapping("/song/submit")
    public ResponseObject uploadSong(@RequestParam("roomId") String roomId,
                                     @RequestParam("song") String song,
                                     @RequestParam("program") String program,
                                     @RequestParam("audio") MultipartFile audio,
                                     @RequestParam("album") MultipartFile image,
                                     @RequestParam("lyric") List<MultipartFile> lyrics
    ) {
        return songService.submit(roomId, song, program, audio, image, lyrics) ? ResponseObject.success(null, "Upload" +
                " " +
                "successfully.") :
                ResponseObject.fail(null, "Upload unsuccessfully.");
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

    @RequestMapping("/song/uploadAlbumCover")
    @LoginRequired
    public ResponseObject uploadAlbumCover(@RequestParam MultipartFile album) {
        return songService.uploadAlbumCover(album) ? ResponseObject.success(null, "Upload successfully.") :
                ResponseObject.fail(null, "Fail to upload album cover.");
    }

    @RequestMapping("/song/uploadLyric")
    public ResponseObject uploadLyric(@RequestParam List<MultipartFile> lyric) {
        return songService.uploadLyric(lyric) ? ResponseObject.success(null, "Upload successfully.") : ResponseObject.fail(null, "Fail to upload lyric.");
    }
}
