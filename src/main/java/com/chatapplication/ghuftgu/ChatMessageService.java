package com.chatapplication.ghuftgu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private RoomService roomService;
    
    public ChatMessage sendMessage(String roomCode, String senderName, String content) {
        roomService.findByRoomCode(roomCode);

        ChatMessage message = new ChatMessage();
        
        message.setSenderName(senderName);
        message.setContent(content);
        message.setRoomCode(roomCode);

        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesByRoomCode(String roomCode) {
        return chatMessageRepository.findByRoomCodeOrderBySentAtAsc(roomCode);
    }

    public ChatMessage saveSystemMessage(ChatMessage message){
        return chatMessageRepository.save(message);
    }
}
