package com.chunkie.live_lyrics_server.repo;

import com.chunkie.live_lyrics_server.entity.Programme;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProgrammeRepository extends MongoRepository<Programme, String> {

    @Query("{programmeId:'?0'}")
    Programme findByProgrammeId(String programmeId);
}
