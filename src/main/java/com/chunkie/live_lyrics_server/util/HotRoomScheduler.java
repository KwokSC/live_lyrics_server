package com.chunkie.live_lyrics_server.util;

import com.chunkie.live_lyrics_server.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;

@Component
public class HotRoomScheduler {

    private static final Logger logger = LoggerFactory.getLogger(HotRoomScheduler.class);

    @Resource
    private RoomService RoomService;

    @Scheduled(fixedRate = 3600000) // 每小时执行一次，单位为毫秒
    public void refreshHotRooms() {
        logger.info("Current time is: {} Hot room list is refreshing.", LocalTime.now());
        RoomService.updateHotRooms();
    }

}
