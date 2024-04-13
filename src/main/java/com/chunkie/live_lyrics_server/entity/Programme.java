package com.chunkie.live_lyrics_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "programmes")
@Data
@AllArgsConstructor
public class Programme {

    @Id
    private String programmeId;

    private List<Program> programList;
}
