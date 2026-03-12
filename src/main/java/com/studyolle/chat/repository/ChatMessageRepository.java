package com.studyolle.chat.repository;

import com.studyolle.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findByStudyIdOrderBySentAtAsc(Long studyId);
}
