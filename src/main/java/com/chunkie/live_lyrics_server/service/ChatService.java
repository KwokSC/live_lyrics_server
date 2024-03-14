package com.chunkie.live_lyrics_server.service;

import com.chunkie.live_lyrics_server.entity.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService {

    private final ConcurrentHashMap<String, List<ChatMessage>> chatHistoryList = new ConcurrentHashMap<>();

    public void handleNewMessage(String roomId, ChatMessage message) {
        List<ChatMessage> chatHistory = chatHistoryList.computeIfAbsent(roomId, k -> new ArrayList<>());
        chatHistory.add(message);
    }
}
