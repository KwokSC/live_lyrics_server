package com.chunkie.live_lyrics_server.entity;

import com.chunkie.live_lyrics_server.dto.UserDTO;
import com.chunkie.live_lyrics_server.util.MusicTimerTask;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Data
@AllArgsConstructor
public class LiveStatus {

    private Room room;

    private Song currentSong;

    private List<UserDTO> userList;

    private Timer timer;

    private MusicTimerTask task;

    public LiveStatus(){
        this.userList = new ArrayList<>();
        this.timer = new Timer();
    }

    @Override
    public String toString() {
        String songInfo;
        Integer currentTimeStr;
        String isPlayingStr;
        if (currentSong != null) {
            songInfo = currentSong.getSongId() + " " + currentSong.getSongName();
        } else {
            songInfo = "No song is active.";
        }
        if (task == null) {
            currentTimeStr = 0;
            isPlayingStr = "Unavailable";
        } else {
            currentTimeStr = task.getCurrentTime();
            isPlayingStr = task.getPlaying().toString();
        }
        return String.format("RoomStatus[song=%s, users=%s, currentTime=%d, isPlaying=%s]", songInfo, userList.toString(), currentTimeStr, isPlayingStr);
    }
}
