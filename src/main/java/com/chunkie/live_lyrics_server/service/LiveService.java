package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.dto.LiveStatusDTO;
import com.chunkie.live_lyrics_server.entity.*;
import com.chunkie.live_lyrics_server.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.chunkie.live_lyrics_server.common.Constants.UserType.*;

@Service
public class LiveService {

    @Resource
    private RoomService roomService;

    @Resource
    private UserService userService;

    private final ConcurrentHashMap<String, LiveStatus> liveStatusList = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(LiveService.class);

    public List<LiveStatusDTO> getAllLiveStatuses() {
        List<LiveStatus> liveStatuses = new ArrayList<>(liveStatusList.values());
        List<LiveStatusDTO> result = new ArrayList<>();
        for (LiveStatus liveStatus : liveStatuses) {
            LiveStatusDTO liveStatusDTO = new LiveStatusDTO();
            liveStatusDTO.setRoomId(liveStatus.getRoom().getRoomId());
            liveStatusDTO.setRoomTitle(liveStatus.getRoom().getRoomTitle());
            liveStatusDTO.setHostInfo(userService.getProfileByAccount(liveStatus.getRoom().getRoomOwner()));
            liveStatusDTO.setAudienceAmount(liveStatus.getUserList().size());
            liveStatusDTO.setSong(liveStatus.getCurrentSong());
            result.add(liveStatusDTO);
        }
        return result;
    }

    public PlayerStatus getPlayerStatusByRoomId(String roomId) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        PlayerStatus playerStatus = new PlayerStatus();
        if (liveStatus != null) {
            playerStatus.setCurrentSong(liveStatus.getCurrentSong());
            playerStatus.setIsPlaying(liveStatus.getTask().getIsPlaying());
            playerStatus.setCurrentTime(liveStatus.getTask().getCurrentTime());
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
            roomStatus.setUsers(new ArrayList<>());
        }
        return roomStatus;
    }

    public void updatePlayerStatusByRoomId(String roomId, PlayerStatus playerStatus) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus == null) return;
        Song song = playerStatus.getCurrentSong();
        liveStatus.setCurrentSong(song);
        liveStatus.getTask().setDuration(song.getSongDuration());
        liveStatus.getTask().setCurrentTime(playerStatus.getCurrentTime());
        liveStatus.getTask().setIsPlaying(playerStatus.getIsPlaying());
    }

    public Boolean startLive(String roomId) {
        try {
            LiveStatus liveStatus = new LiveStatus();
            Room room = roomService.getRoomByRoomId(roomId);
            liveStatus.setRoom(room);
            liveStatus.getTimer().schedule(liveStatus.getTask(), 0, 1000);
            liveStatusList.put(roomId, liveStatus);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
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
        logger.info("End live for roomId: {}", roomId);
    }

    public String userEnter(String roomId, String account) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus != null) {
            UserDTO userDTO = generateUserDTOByAccount(account);
            userDTO.setType(liveStatus.getRoom().getRoomOwner().equals(account) ? HOST : AUDIENCE);
            liveStatus.getUserList().put(account, userDTO);
            logger.info("{} enter the room_{}", userDTO, roomId);
            logger.info(liveStatus.toString());
            return userDTO.getType();
        }
        return null;
    }

    public String userExit(String roomId, String userId) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus != null) {
            UserDTO userDTO = liveStatus.getUserList().get(userId);
            liveStatus.getUserList().remove(userId);
            logger.info("{} exit the room_{}", userId, roomId);
            return userDTO.getType();
        }
        return null;
    }

    private UserDTO generateUserDTOByAccount(String account) {
        UserDTO userDTO = new UserDTO();
        User user = userService.getUserByAccount(account);
        if (user == null) {
            userDTO.setUserAccount(account);
            userDTO.setUserName("guest_" + account);
        } else {
            userDTO.setUserAccount(user.getUserAccount());
            userDTO.setUserName(user.getUserName());
        }
        return userDTO;
    }

    public void updateHotRooms(){

    }
}
