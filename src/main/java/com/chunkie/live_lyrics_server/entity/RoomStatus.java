package com.chunkie.live_lyrics_server.entity;

import com.chunkie.live_lyrics_server.dto.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class RoomStatus {

    private Boolean isOnline;

    private List<UserDTO> users;

}
