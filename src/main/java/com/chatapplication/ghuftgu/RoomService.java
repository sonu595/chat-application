package com.chatapplication.ghuftgu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public Room createRoom(String roomName, String password){
        
        if(roomRepository.findByName(roomName).isPresent()){
            throw new RuntimeException("Room already exists: "+roomName);
        }
        
        Room room = new Room();
        room.setName(roomName);
        room.setPassword(password);
        String roomCode = generateRoomCode();
        room.setRoomCode(roomCode);
        
        return roomRepository.save(room);
    }

    public Room findByRoomCode(String roomCode){
        return roomRepository.findByRoomCode(roomCode)
            .orElseThrow(() -> new RuntimeException("Room not found: "+roomCode));
    }
    

    private String generateRoomCode() {
        int maxAttempts = 10;
        for(int i = 0; i < maxAttempts; i++) {
            StringBuilder code = new StringBuilder();
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            for(int j = 0; j < 8; j++) { 
                code.append(chars.charAt((int)(Math.random() * chars.length())));
            }
            if(!roomRepository.findByRoomCode(code.toString()).isPresent()) {
                return code.toString();
            }
        }
        throw new RuntimeException("Unable to generate unique room code");
    }
}
