package com.chunkie.live_lyrics_server.entity;

import lombok.Data;

import java.util.List;

@Data
public class Program {

    private String songId;

    private List<Recommendation> recommendations;
}
