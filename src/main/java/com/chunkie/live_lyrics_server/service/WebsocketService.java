package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.common.MessageObject;
import com.chunkie.live_lyrics_server.entity.ChatMessage;
import com.chunkie.live_lyrics_server.entity.PlayerStatus;
import com.chunkie.live_lyrics_server.entity.RoomStatus;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.chunkie.live_lyrics_server.common.Constants.MsgType.*;

@Service
public class WebsocketService {

    @Resource
    private Gson gson;

    @Resource
    private LiveService liveService;

    @Resource
    private ChatService chatService;

    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);

    public MessageObject handleStatusMessage(String message, String messageType, String roomId) {
        MessageObject messageObject = new MessageObject();
        switch (messageType) {
            case PLAYER:
                PlayerStatus playerStatus = gson.fromJson(message, PlayerStatus.class);
                logger.info("This message is player type. " + "Content:\n" + playerStatus.toString());
                liveService.updatePlayerStatusByRoomId(roomId, playerStatus);
                messageObject.setType(PLAYER);
                messageObject.setData(playerStatus);
                break;
            case ROOM:
                RoomStatus roomStatus = gson.fromJson(message, RoomStatus.class);
                logger.debug("This message is room type." + "Content:\n" + roomStatus.toString());
                messageObject.setType(ROOM);
                messageObject.setData(roomStatus);
                break;
        }
        return messageObject;
    }

    public MessageObject subscribeRoom(String roomId, String userId){
        MessageObject messageObject = new MessageObject();
        messageObject.setType(SUBSCRIBE);
        messageObject.setData(liveService.subscribeRoom(roomId, userId));
        return messageObject;
    }

    public MessageObject userEnter(String roomId){
        MessageObject messageObject = new MessageObject();
        messageObject.setType(USER_ENTER);
        messageObject.setData(liveService.getLiveStatusByRoomId(roomId));
        return messageObject;
    }

    public MessageObject userExit(String roomId, String userId){
        MessageObject messageObject = new MessageObject();
        messageObject.setType(USER_EXIT);
        liveService.unsubscribeRoom(roomId, userId);
        messageObject.setData(liveService.getLiveStatusByRoomId(roomId));
        return messageObject;
    }

    public MessageObject handleChatMessage(String message, String roomId){
        MessageObject messageObject = new MessageObject();
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        logger.debug("This message is chat type." + "Content:\n" + chatMessage.toString());
        chatService.handleNewMessage(roomId, chatMessage);
        messageObject.setType(CHAT);
        messageObject.setData(chatMessage);
        return messageObject;
    }

}
