package com.chunkie.live_lyrics_server.entity.response;

import com.chunkie.live_lyrics_server.entity.UserInfo;
import lombok.Data;

@Data
public class LoginResponse {

    private String auth;

    private UserInfo userInfo;
}
