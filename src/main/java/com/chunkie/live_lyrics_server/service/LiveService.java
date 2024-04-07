package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.entity.*;
import com.chunkie.live_lyrics_server.dto.UserDTO;
import com.chunkie.live_lyrics_server.util.MusicTimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static com.chunkie.live_lyrics_server.dto.UserDTO.UserType.*;

@Service
public class LiveService {

    @Resource
    private RoomService roomService;

    @Resource
    private SongService songService;

    @Resource
    private UserService userService;

    private final ConcurrentHashMap<String, LiveStatus> liveStatusList = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(LiveService.class);

    public PlayerStatus getPlayerStatusByRoomId(String roomId) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        PlayerStatus playerStatus = new PlayerStatus();
        if (liveStatus != null) {
            playerStatus.setCurrentSong(liveStatus.getCurrentSong() != null ? liveStatus.getCurrentSong().getSongId() : null);
            playerStatus.setIsPlaying(liveStatus.getTask() != null ? liveStatus.getTask().getPlaying() : false);
            playerStatus.setCurrentTime(liveStatus.getTask() != null ? liveStatus.getTask().getCurrentTime() : 0);
            return playerStatus;
        }
        return playerStatus;
    }

    public RoomStatus getRoomStatusByRoomId(String roomId) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        RoomStatus roomStatus = new RoomStatus();
        if (liveStatus != null) {
            roomStatus.setIsOnline(true);
            roomStatus.setUsers(new ArrayList<>(liveStatus.getUserList().values()));
        } else {
            roomStatus.setIsOnline(false);
            roomStatus.setUsers(null);
        }
        return roomStatus;
    }

    public void updatePlayerStatusByRoomId(String roomId, PlayerStatus playerStatus) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus == null) return;
        if (liveStatus.getCurrentSong() == null || !liveStatus.getCurrentSong().getSongId().equals(playerStatus.getCurrentSong())) {
            Song song = songService.getSongById(playerStatus.getCurrentSong());
            liveStatus.setCurrentSong(song);
            liveStatus.setTask(new MusicTimerTask());
            liveStatus.getTask().setDuration(song.getSongDuration());
            liveStatus.getTimer().schedule(liveStatus.getTask(), 0, 1000);
        }
        liveStatus.getTask().setCurrentTime(playerStatus.getCurrentTime());
        liveStatus.getTask().setPlaying(playerStatus.getIsPlaying());
        logger.info(liveStatus.toString());
    }

    public Boolean startLive(String roomId) {
        try {
            LiveStatus liveStatus = new LiveStatus();
            Room room = roomService.getRoomByRoomId(roomId);
            liveStatus.setRoom(room);
            liveStatusList.put(roomId, liveStatus);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void endLive(String roomId) {
        LiveStatus status = liveStatusList.get(roomId);
        if (status != null) {
            status.getTimer().cancel();
            if (status.getTask() != null) {
                status.getTask().cancel();
            }
            liveStatusList.remove(roomId);
        }
        logger.info(liveStatusList.toString());
    }

    public void userEnter(String roomId, String userId) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus != null) {
            UserDTO userDTO = generateUserDTOById(userId);
            userDTO.setType(liveStatus.getRoom().getRoomOwner().equals(userId) ? HOST : UserDTO.UserType.AUDIENCE);
            liveStatus.getUserList().put(userId, userDTO);
            logger.info("{} enter the room_{}", userDTO, roomId);
            logger.info(liveStatus.toString());
        }
    }

    public void userExit(String roomId, String userId) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus != null) {
            liveStatus.getUserList().remove(userId);
            logger.info("{} exit the room_{}", userId, roomId);
            logger.info(liveStatus.toString());
        }
    }

    private UserDTO generateUserDTOById(String id) {
        UserDTO userDTO = new UserDTO();
        User user = userService.getUserById(id);
        if (user == null) {
            userDTO.setUserAccount(id);
            userDTO.setUserName("guest_" + id);
        } else {
            userDTO.setUserAccount(user.getUserAccount());
            userDTO.setUserName(user.getUserName());
        }
        return userDTO;
    }
}
