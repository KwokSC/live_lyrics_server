package com.chunkie.live_lyrics_server.util;

import com.chunkie.live_lyrics_server.entity.PlayStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class RoomPlayerManager {
    @Resource
    private ScheduledExecutorService executorService;

    @Resource
    private ConcurrentHashMap<String, PlayStatus> playStatusList;


}
