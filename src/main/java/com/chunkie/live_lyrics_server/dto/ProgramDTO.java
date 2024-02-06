package com.chunkie.live_lyrics_server.dto;

import com.chunkie.live_lyrics_server.entity.Recommendation;
import com.chunkie.live_lyrics_server.entity.Song;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class ProgramDTO {

    @Id
    private Song song;

    private List<Recommendation> recommendations;
}
