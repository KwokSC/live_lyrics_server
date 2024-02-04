package com.chunkie.live_lyrics_server.dto;

import com.chunkie.live_lyrics_server.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SongDTO {

    private Song song;

    private String albumCoverURL;
}
