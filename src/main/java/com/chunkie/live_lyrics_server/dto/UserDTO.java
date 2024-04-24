package com.chunkie.live_lyrics_server.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String type;

    private String userAccount;

    private String userName;

    @Override
    public String toString(){
        return String.format("User[type=%s, account=%s, name=%s]", type, userAccount, userName);
    }
}
