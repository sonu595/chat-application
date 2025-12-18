package com.chatapplication.ghuftgu;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {
    @Autowired
    private  RoomService roomService;

    @Autowired
    private ParticipantService participantService;

    @Autowired 
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private void validateRoomCode(String roomCode) {
        if (roomCode == null || roomCode.trim().isEmpty() || roomCode.length() < 6) {
            throw new RuntimeException("Invalid room code format");
        }
    }
    
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() > 50) {
            throw new RuntimeException("Invalid name - must be 1-50 characters");
        }
    }

    private ResponseEntity<?> handleOperation(Supplier<ResponseEntity<?>> operation) {
        try {
            return operation.get();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody Room roomRequest) {
        return handleOperation(() -> {
            validateName(roomRequest.getName());
            if (roomRequest.getPassword() == null || roomRequest.getPassword().isEmpty()) {
                throw new RuntimeException("password is required");
            }
            Room room = roomService.createRoom(roomRequest.getName(), roomRequest.getPassword());
            return ResponseEntity.ok(room);
        });
    }

    // 2 get room
    @GetMapping("/{roomCode}")
    public ResponseEntity<Room> getRoom(@PathVariable String roomCode){
        Room room = roomService.findByRoomCode(roomCode);
        return ResponseEntity.ok(room);
    }

    // 3 join room
    @PostMapping("/{roomCode}/join")
    public ResponseEntity<?> joinRoom(@PathVariable String roomCode, @RequestBody JoinRequest request) {
        return handleOperation(() -> {
            validateRoomCode(roomCode);
            validateName(request.getName());

            Participant participant = participantService.joinRoom(roomCode, request.getName(), request.getAge(),request.getPassword());

            ChatMessage systemMessage = new ChatMessage();
            systemMessage.setSenderName("system");
            systemMessage.setContent(request.getName() + " has joined the room");
            systemMessage.setRoomCode(roomCode);
            systemMessage.setType(MessageType.JOIN);
            chatMessageService.saveSystemMessage(systemMessage);
            messagingTemplate.convertAndSend("/topic/room/"+roomCode,systemMessage);
            return ResponseEntity.ok(participant);
        });
    }

    @GetMapping("/{roomCode}/participants")
    public ResponseEntity<?> getParticipants(@PathVariable String roomCode) {
        return handleOperation(() -> {
            validateRoomCode(roomCode);
            Room room = roomService.findByRoomCode(roomCode);
            List<Participant> participants = participantService.getParticipantsByRoomId(room.getId());
            return ResponseEntity.ok(participants);
        });
    }
    // 5 sent message

    @PostMapping("/{roomCode}/messages")
    public ResponseEntity<?> sendMessage(@PathVariable String roomCode, @RequestBody ChatMessage messageRequest) {
        return handleOperation(() -> {
            validateRoomCode(roomCode);
            validateName(messageRequest.getSenderName());
            ChatMessage message = chatMessageService.sendMessage(roomCode, messageRequest.getSenderName(), messageRequest.getContent());
            return ResponseEntity.ok(message);
        });
    }

    @GetMapping("/{roomCode}/messages")
    public ResponseEntity<?> getMessages(@PathVariable String roomCode) {
        return handleOperation(() -> {
            validateRoomCode(roomCode);
            Room room = roomService.findByRoomCode(roomCode);
            return ResponseEntity.ok(chatMessageService.getMessagesByRoomCode(room.getRoomCode()));
        });
    }
    
    @MessageMapping("/chat/{roomCode}")
    @SendTo("/topic/room/{roomCode}")
    public ChatMessage sendChatMessage(
            @DestinationVariable String roomCode, 
            ChatMessage message) {
        return chatMessageService.sendMessage(roomCode, message.getSenderName(), message.getContent());
    }
}
