package com.chunkie.live_lyrics_server.entity;

import com.chunkie.live_lyrics_server.common.Gender;
import lombok.Data;

@Data
public class User {

    private String userAccount;

    private String userPassword;

    private String userName;

    private String summary;

    private Gender gender;
}
