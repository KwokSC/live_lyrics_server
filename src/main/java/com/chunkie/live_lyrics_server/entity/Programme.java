package com.chunkie.live_lyrics_server.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "programmes")
@Data
public class Programme {

    @Id
    private String roomId;

    private List<Program> programList;
}
