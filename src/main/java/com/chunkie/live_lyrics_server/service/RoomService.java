package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.mapper.RoomMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoomService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RoomMapper roomMapper;
}
