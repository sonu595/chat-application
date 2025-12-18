package com.chatapplication.ghuftgu;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByRoomId(Long roomId);

    Optional<Participant> findByRoomIdAndName(Long roomId, String name);
}