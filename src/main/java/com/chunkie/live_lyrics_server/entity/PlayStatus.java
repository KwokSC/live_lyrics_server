package com.chunkie.live_lyrics_server.entity;

import lombok.Data;

@Data
public class PlayStatus {

    private String currentSong;

    private String currentTime;

    private Boolean isPlaying;

}