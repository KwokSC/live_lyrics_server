package com.chunkie.live_lyrics_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStatus {

    private Song currentSong = null;

    private Integer currentTime = 0;

    private Boolean isPlaying = false;

}