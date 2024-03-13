package com.chunkie.live_lyrics_server.entity.response;

import com.chunkie.live_lyrics_server.entity.RoomStatus;
import com.chunkie.live_lyrics_server.entity.PlayerStatus;
import lombok.Data;

@Data
public class SubscribeResponse {

    private RoomStatus roomStatus;

    private PlayerStatus playerStatus;
}
