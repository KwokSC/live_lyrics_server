package com.chunkie.live_lyrics_server.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageObject {

    private Object data;

    private String type;
}
