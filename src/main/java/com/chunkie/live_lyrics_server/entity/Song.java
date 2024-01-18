package com.chunkie.live_lyrics_server.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Song {

    private String songId;

    private String songName;

    private String songArtist;

    private String songAlbum;

    private Integer songDuration;

}