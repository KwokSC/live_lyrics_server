package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.mapper.SongMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
public class SongService {

    @Resource
    private SongMapper songMapper;

    public boolean uploadSong(Song song) {
        return songMapper.addSong(song) != 0;
    }

    public Song getSongById(String songId) {
        return songMapper.getSongById(songId);
    }

    public void deleteSongById(String songId) {
        songMapper.deleteSongById(songId);
    }
}
