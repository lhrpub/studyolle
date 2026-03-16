package com.studyolle.chat.service;

import com.studyolle.chat.dto.ChatMessageDTO;
import com.studyolle.chat.entity.ChatMessage;
import com.studyolle.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;

    public Slice<ChatMessageDTO> getChatMessages(Long studyId, Pageable pageable){
        Slice<ChatMessage> messages = chatMessageRepository.findByStudyIdOrderBySentAtDesc(studyId, pageable);

        return messages.map(ChatMessageDTO::from);
    }
}
