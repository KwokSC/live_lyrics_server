package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.mapper.SongMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SongService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SongMapper songMapper;
}
