package com.chunkie.live_lyrics_server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "playStatus")
@Data
public class PlayStatus {

    @Id
    private String roomId;

    private String currentSongId;

    private String currentTime;

    private Boolean isPaused;

}