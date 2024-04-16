package com.chunkie.live_lyrics_server.util;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

@Getter
@Setter
public class MusicTimerTask extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(MusicTimerTask.class);

    private Integer currentTime = 0;

    private Integer duration = 0;

    private Boolean isPlaying = false;

    @Override
    public void run() {
        if (isPlaying && currentTime < duration) {
            currentTime++;
        } else if (currentTime >= duration) {
            currentTime = duration;
            this.setIsPlaying(false);
        }
    }

    @Override
    public boolean cancel() {
        this.setIsPlaying(false);
        return super.cancel();
    }
}
