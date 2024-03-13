package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.common.MessageObject;
import com.chunkie.live_lyrics_server.entity.PlayerStatus;
import com.chunkie.live_lyrics_server.entity.RoomStatus;
import com.chunkie.live_lyrics_server.dto.UserDTO;
import com.chunkie.live_lyrics_server.entity.LiveStatus;
import com.chunkie.live_lyrics_server.entity.Song;
import com.chunkie.live_lyrics_server.entity.User;
import com.chunkie.live_lyrics_server.entity.response.SubscribeResponse;
import com.chunkie.live_lyrics_server.util.MusicTimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LiveService {

    @Resource
    private RoomService roomService;

    @Resource
    private SongService songService;

    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;

    private final ConcurrentHashMap<String, LiveStatus> liveStatusList;

    private static final Logger logger = LoggerFactory.getLogger(LiveService.class);

    public LiveService() {
        this.liveStatusList = new ConcurrentHashMap<>();
    }

    public PlayerStatus getPlayerStatusByRoomId(String roomId) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus != null) {
            PlayerStatus playerStatus = new PlayerStatus();
            playerStatus.setCurrentSong(liveStatus.getCurrentSong() != null ? liveStatus.getCurrentSong().getSongId() : null);
            playerStatus.setIsPlaying(liveStatus.getTask() != null ? liveStatus.getTask().getPlaying() : false);
            playerStatus.setCurrentTime(liveStatus.getTask() != null ? liveStatus.getTask().getCurrentTime() : 0);
            return playerStatus;
        }
        return null;
    }

    public RoomStatus getLiveStatusByRoomId(String roomId) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus != null) {
            RoomStatus roomStatus = new RoomStatus();
            roomStatus.setIsOnline(true);
            roomStatus.setUsers(liveStatus.getUserList());
            return roomStatus;
        }
        return null;
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
            liveStatus.setRoom(roomService.getRoomByRoomId(roomId));
            liveStatusList.put(roomId, liveStatus);
            logger.info(liveStatusList.toString());
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

    public SubscribeResponse subscribeRoom(String roomId, String userId) {
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus != null) {
            UserDTO userDTO = generateUserDTOById(userId);
            userDTO.setType(liveStatus.getRoom().getRoomOwner().equals(userId) ? UserDTO.UserType.HOST : UserDTO.UserType.AUDIENCE);
            logger.info(userDTO.toString() + " enter the room_" + roomId);
            liveStatus.getUserList().add(userDTO);
            logger.info(liveStatus.toString());
        }
        SubscribeResponse response = new SubscribeResponse();
        response.setRoomStatus(getLiveStatusByRoomId(roomId));
        response.setPlayerStatus(getPlayerStatusByRoomId(roomId));
        return response;
    }

    public void unsubscribeRoom(String roomId, String userId){
        LiveStatus liveStatus = liveStatusList.get(roomId);
        if (liveStatus != null) {
            List<UserDTO> users = liveStatus.getUserList();
            for (UserDTO user : users){
                if (user.getUserAccount() == userId){
                    users.remove(user);
                    break;
                }
            }
            logger.info(userId + " exit the room_" + roomId);
            logger.info(liveStatus.toString());
        }
    }

    private UserDTO generateUserDTOById(String id) {
        UserDTO userDTO = new UserDTO();
        User user = userService.getUserById(id);
        if (user == null) {
            userDTO.setUserAccount(id);
            userDTO.setUserName("audience_" + id);
        } else {
            userDTO.setUserAccount(user.getUserAccount());
            userDTO.setUserName(user.getUserName());
        }
        return userDTO;
    }
}
