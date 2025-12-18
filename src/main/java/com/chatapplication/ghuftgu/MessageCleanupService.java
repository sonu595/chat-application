package com.chatapplication.ghuftgu;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class MessageCleanupService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deleteOldMessages() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);

        chatMessageRepository.deleteBySentAtBefore(cutoffTime);

        System.out.println("delete old messages: "+LocalDateTime.now());
    }
    
}
