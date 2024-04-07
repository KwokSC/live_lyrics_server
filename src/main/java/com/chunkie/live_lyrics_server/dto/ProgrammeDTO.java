package com.chunkie.live_lyrics_server.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProgrammeDTO {

    @Id
    private String programmeId;

    private List<ProgramDTO> programList = new ArrayList<>();
}
