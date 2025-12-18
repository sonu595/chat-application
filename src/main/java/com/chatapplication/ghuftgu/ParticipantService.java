package com.chatapplication.ghuftgu;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;
    
    @Autowired
    private RoomService roomService;


     public Participant joinRoom(String roomCode, String name, Integer age, String password) {
       Room room = roomService.findByRoomCode(roomCode);
       

       if (!room.getPassword().equals(password)) {
        throw new RuntimeException("Indalid Room Password!");
       }

        if(participantRepository.findByRoomIdAndName(room.getId(), name).isPresent()){
             throw new RuntimeException("User" + name + "alredy exists in this room");
         }
        Participant participant = new Participant();
        participant.setName(name);
        participant.setAge(age);
        participant.setRoom(room);

        return participantRepository.save(participant);
     }


    public java.util.List<Participant> getParticipantsByRoomId(Long roomId) {
    return participantRepository.findByRoomId(roomId);
    }
}