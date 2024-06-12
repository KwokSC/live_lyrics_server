package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.common.MessageObject;
import com.chunkie.live_lyrics_server.entity.ChatMessage;
import com.chunkie.live_lyrics_server.entity.PlayerStatus;
import com.chunkie.live_lyrics_server.entity.RoomStatus;
import com.chunkie.live_lyrics_server.exception.NoTypeMessageException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static com.chunkie.live_lyrics_server.common.Constants.MsgType.*;
import static com.chunkie.live_lyrics_server.common.Constants.UserType.*;

@Service
public class WebsocketService {

    @Resource
    private Gson gson;

    @Resource
    private LiveService liveService;

    @Resource
    private ChatService chatService;

    @Value("${live.auto-end-live}")
    private int autoEndLive;

    private final Map<String, WebSocketSession> sessionPool = new ConcurrentHashMap<>();

    private final Map<String, String> roomSessionMap = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);

    /**
     * @Description Handle different types of websocket message and call related functions.
     * @Param [message, messageType, roomId]
     * @Return com.chunkie.live_lyrics_server.common.MessageObject
     * @Author chunkie
     * @Date 3/19/24
     */
    public MessageObject handleStatusMessage(String message, String messageType, String roomId) {
        MessageObject messageObject = new MessageObject();
        switch (messageType) {
            case PLAYER:
                PlayerStatus playerStatus = gson.fromJson(message, PlayerStatus.class);
                logger.info("This message is {} type. Content:\n{}", PLAYER, playerStatus.toString());
                liveService.updatePlayerStatusByRoomId(roomId, playerStatus);
                messageObject.setType(PLAYER);
                messageObject.setData(playerStatus);
                break;
            case ROOM:
                RoomStatus roomStatus = gson.fromJson(message, RoomStatus.class);
                logger.info("This message is {} type.Content:\n{}", ROOM, roomStatus.toString());
                messageObject.setType(ROOM);
                messageObject.setData(roomStatus);
                break;
            default:
                throw new NoTypeMessageException();
        }
        return messageObject;
    }

    /**
     * @Description The function will be called each time a new user subscribe a specific room topic.
     * @Param [roomId, userId]
     * @Return com.chunkie.live_lyrics_server.common.MessageObject
     * @Author chunkie
     * @Date 3/19/24
     */
    public MessageObject subscribeRoom(String roomId) {
        MessageObject messageObject = new MessageObject();
        messageObject.setType(PLAYER);
        messageObject.setData(liveService.getPlayerStatusByRoomId(roomId));
        return messageObject;
    }

    /**
     * @Description The function will be called right after a new user subscribe a specific room topic. (Please see
     * {@link #subscribeRoom(String) It will return a updated user list.
     * @Param [roomId]
     * @Return com.chunkie.live_lyrics_server.common.MessageObject
     * @Author chunkie
     * @Date 3/19/24
     */
    public MessageObject userEnter(String roomId, String userId, String sessionId) {
        if (Objects.equals(liveService.userEnter(roomId, userId), HOST)) {
            roomSessionMap.put(sessionId, roomId);
            logger.info("Room {} entered session {}", roomId, sessionId);
        }
        MessageObject messageObject = new MessageObject();
        messageObject.setType(USER_ENTER);
        messageObject.setData(liveService.getRoomStatusByRoomId(roomId));
        return messageObject;
    }

    /**
     * @Description The function will be called each time a user exit the room. It will call
     * {@link LiveService#userExit(String, String)} first and then return an encapsulated
     * MessageObject including the updated user list.
     * @Param [roomId, userId]
     * @Return com.chunkie.live_lyrics_server.common.MessageObject
     * @Author chunkie
     * @Date 3/19/24
     */
    public MessageObject userExit(String roomId, String userId) {
        liveService.userExit(roomId, userId);
        MessageObject messageObject = new MessageObject();
        messageObject.setType(USER_EXIT);
        messageObject.setData(liveService.getRoomStatusByRoomId(roomId));
        return messageObject;
    }

    /**
     * @Description The function to handle chat message and broadcast.
     * @Param [message, roomId]
     * @Return com.chunkie.live_lyrics_server.common.MessageObject
     * @Author chunkie
     * @Date 2024/5/16
     */
    public MessageObject handleChatMessage(String message, String roomId) {
        MessageObject messageObject = new MessageObject();
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        logger.info("This message is chat type.Content:\n{}", chatMessage.toString());
        chatService.handleNewMessage(roomId, chatMessage);
        messageObject.setType(CHAT);
        messageObject.setData(chatMessage);
        return messageObject;
    }

    public MessageObject handleTipFlag(){
        MessageObject messageObject = new MessageObject();
        messageObject.setType(TIP);
        return messageObject;
    }


    public void deactivateSession(String sessionId) throws IOException {
        WebSocketSession session = sessionPool.get(sessionId);
        if (session != null) {
            session.close();
            String idleRoom = roomSessionMap.get(sessionId);
            roomSessionMap.remove(sessionId);

            // Automatically end live after session is deactivated for 300 seconds
            if (idleRoom != null) {
                logger.info("Room {} is now idle", idleRoom);
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    int counter = autoEndLive;
                    @Override
                    public void run() {
                        if (counter >= 0) {
                            counter--;
                        }else {
                            if (!roomSessionMap.containsValue(idleRoom)) {
                                liveService.endLive(idleRoom);
                            }
                            timer.cancel();
                        }
                    }
                };
                timer.schedule(timerTask, 0,1000);
            }
        }
        sessionPool.remove(sessionId);
        logger.info("Deactivate session: {}", sessionId);
    }

    public void activateSession(String id, WebSocketSession session) {
        sessionPool.put(id, session);
    }
}
