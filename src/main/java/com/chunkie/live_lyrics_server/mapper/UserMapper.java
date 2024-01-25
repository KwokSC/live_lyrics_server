package com.chunkie.live_lyrics_server.mapper;

import com.chunkie.live_lyrics_server.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User getUserById(String id);
}
