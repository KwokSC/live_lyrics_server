package com.chunkie.live_lyrics_server.repo;

import com.chunkie.live_lyrics_server.entity.Programme;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProgrammeRepository extends MongoRepository<Programme, String> {

    Programme findByProgrammeId(String programmeId);
}
