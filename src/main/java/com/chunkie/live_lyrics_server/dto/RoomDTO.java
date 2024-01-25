package com.chunkie.live_lyrics_server.dto;

import com.chunkie.live_lyrics_server.entity.PlayStatus;
import com.chunkie.live_lyrics_server.entity.Programme;
import lombok.Data;

@Data
public class RoomDTO {

    private Programme programme;

    private PlayStatus playStatus;
}
