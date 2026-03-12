package com.studyolle.chat.repository;

import com.studyolle.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findByStudyIdOrderBySentAtAsc(Long studyId);

    Slice<ChatMessage> findByStudyIdOrderBySentAtDesc(Long studyId, Pageable pageable);

}
