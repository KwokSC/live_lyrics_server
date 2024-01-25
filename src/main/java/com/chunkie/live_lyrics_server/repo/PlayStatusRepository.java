package com.chunkie.live_lyrics_server.repo;

import com.chunkie.live_lyrics_server.entity.PlayStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayStatusRepository extends MongoRepository<PlayStatus, String> {

    PlayStatus getPlayStatusByRoomId(String roomId);
}
