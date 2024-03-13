package com.chunkie.live_lyrics_server.util;

import java.util.TimerTask;

public class MusicTimerTask extends TimerTask {

    public Integer getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Integer currentTime) {
        this.currentTime = currentTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
    }

    public synchronized void pauseTimer() {
        this.isPlaying = false;
    }

    public synchronized void resumeTimer() {
        this.isPlaying = true;
        notify();
    }

    private Integer currentTime;

    private Integer duration;

    private Boolean isPlaying;

    @Override
    public void run() {
        if (isPlaying && currentTime <= duration) {
            currentTime++;
        }
        else if (currentTime > duration){
            currentTime = duration;
            pauseTimer();
        }
    }

    @Override
    public boolean cancel() {
        pauseTimer();
        return super.cancel();
    }
}
