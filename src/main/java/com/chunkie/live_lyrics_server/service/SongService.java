package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.dto.SongDTO;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.mapper.SongMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SongService {

    @Resource
    private SongMapper songMapper;

    @Resource
    private S3Service s3Service;


    public SongDTO getSongInfo(String songId){
        return new SongDTO(songMapper.getSongById(songId),s3Service.getFile("/resources/images/" + songId));
    }

    public Boolean uploadSongInfo(Song song){
        return songMapper.addSong(song) != 0;
    }

}
