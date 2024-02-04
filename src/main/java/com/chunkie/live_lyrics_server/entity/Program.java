package com.chunkie.live_lyrics_server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Program {

    @Id
    private String programId;

    private Song song;

    private List<Recommendation> recommendations;
}
