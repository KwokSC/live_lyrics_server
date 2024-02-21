package com.chunkie.live_lyrics_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LyricDTO {
    private String language;
    private String content;
}
