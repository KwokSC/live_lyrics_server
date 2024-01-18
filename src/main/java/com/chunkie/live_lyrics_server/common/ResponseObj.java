package com.chunkie.live_lyrics_server.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObj {

    private Object data;

    private int code;

    private String msg;

}
