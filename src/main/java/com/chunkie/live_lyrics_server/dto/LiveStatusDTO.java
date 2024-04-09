package com.chunkie.live_lyrics_server.dto;

import com.chunkie.live_lyrics_server.entity.Profile;
import com.chunkie.live_lyrics_server.entity.Song;
import lombok.Data;

@Data
public class LiveStatusDTO {

    private String roomId;

    private String roomTitle;

    private Profile hostInfo;

    private Integer audienceAmount;

    private Song song;
}
