package com.chunkie.live_lyrics_server.dto;

import com.chunkie.live_lyrics_server.entity.Recommendation;
import com.chunkie.live_lyrics_server.entity.Song;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProgramDTO {

    private Song song;

    private List<Recommendation> recommendations = new ArrayList<>();
}
