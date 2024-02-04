package com.chunkie.live_lyrics_server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Recommendation {

    @Id
    private String recommendationId;

    private String type;

    private String content;
}
