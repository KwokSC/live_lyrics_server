package com.chunkie.live_lyrics_server.mapper;

import com.chunkie.live_lyrics_server.entity.Song;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SongMapper {

    int addSong(Song song);

    Song getSongById(String id);
}
