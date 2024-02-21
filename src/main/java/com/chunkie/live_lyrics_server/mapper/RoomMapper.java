package com.chunkie.live_lyrics_server.mapper;

import com.chunkie.live_lyrics_server.entity.Room;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper {

    int createRoom(Room room);

    int updateRoomById(Room room);

    Room getRoomByRoomId(String roomId);

    Room getRoomByUserId(String userId);
}
