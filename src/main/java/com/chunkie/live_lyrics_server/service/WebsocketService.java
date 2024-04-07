package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.common.MessageObject;
import com.chunkie.live_lyrics_server.entity.ChatMessage;
import com.chunkie.live_lyrics_server.entity.PlayerStatus;
import com.chunkie.live_lyrics_server.entity.RoomStatus;
import com.chunkie.live_lyrics_server.exception.NoTypeMessageException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.chunkie.live_lyrics_server.common.Constants.MsgType.*;

@Service
public class WebsocketService {

    @Resource
    private Gson gson;

    @Resource
    private LiveService liveService;

    @Resource
    private ChatService chatService;

    private final Map<String, WebSocketSession> sessionPool = new ConcurrentHashMap<>();

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
                logger.info("This message is player type. Content:\n{}", playerStatus.toString());
                liveService.updatePlayerStatusByRoomId(roomId, playerStatus);
                messageObject.setType(PLAYER);
                messageObject.setData(playerStatus);
                break;
            case ROOM:
                RoomStatus roomStatus = gson.fromJson(message, RoomStatus.class);
                logger.info("This message is room type.Content:\n{}", roomStatus.toString());
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
     * {@link #subscribeRoom(String}) It will return a updated user list.
     * @Param [roomId]
     * @Return com.chunkie.live_lyrics_server.common.MessageObject
     * @Author chunkie
     * @Date 3/19/24
     */
    public MessageObject userEnter(String roomId, String userId) {
        MessageObject messageObject = new MessageObject();
        messageObject.setType(USER_ENTER);
        liveService.userEnter(roomId, userId);
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
        MessageObject messageObject = new MessageObject();
        messageObject.setType(USER_EXIT);
        liveService.userExit(roomId, userId);
        messageObject.setData(liveService.getRoomStatusByRoomId(roomId));
        return messageObject;
    }

    public MessageObject handleChatMessage(String message, String roomId) {
        MessageObject messageObject = new MessageObject();
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        logger.info("This message is chat type.Content:\n{}", chatMessage.toString());
        chatService.handleNewMessage(roomId, chatMessage);
        messageObject.setType(CHAT);
        messageObject.setData(chatMessage);
        return messageObject;
    }

    public void activateSession(String sessionId, WebSocketSession session) {
        sessionPool.put(sessionId, session);
        logger.info("Add a session. All connected sessions: {}", sessionPool);
    }

    public void deactivateSession(String sessionId) {
        WebSocketSession session = sessionPool.get(sessionId);
        try {
            if (session != null) session.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        sessionPool.remove(sessionId);
        logger.info("Deactivate session: {}", sessionId);
    }

}
