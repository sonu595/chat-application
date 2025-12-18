package com.chatapplication.ghuftgu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomCodeOrderBySentAtAsc(String roomCode);

    void deleteBySentAtBefore(LocalDateTime cutofftime);
}
