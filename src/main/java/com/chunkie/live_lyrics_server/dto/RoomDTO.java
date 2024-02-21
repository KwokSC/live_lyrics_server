package com.chunkie.live_lyrics_server.dto;

import com.chunkie.live_lyrics_server.entity.PlayStatus;
import lombok.Data;

@Data
public class RoomDTO {

    private String roomId;

    private Integer activeOnline;

    private PlayStatus playStatus;
}
