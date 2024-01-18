package com.chunkie.live_lyrics_server.entity;

import lombok.Data;

@Data
public class Room {

    private String roomId;

    private String roomTitle;

    private Boolean roomStatus;

    private String roomOwner;

}
