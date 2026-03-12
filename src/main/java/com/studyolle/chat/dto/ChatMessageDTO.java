package com.studyolle.chat.dto;

import com.studyolle.chat.entity.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDTO {

    private Long id;
    private Long studyId;
    private Long senderId;
    private String senderName;
    private String message;
    private LocalDateTime sentAt;

    public static ChatMessageDTO from(ChatMessage entity){

        ChatMessageDTO dto = new ChatMessageDTO();

        dto.setId(entity.getId());
        dto.setStudyId(entity.getStudyId());
        dto.setSenderId(entity.getSenderId());
        dto.setSenderName(entity.getSenderName());
        dto.setMessage(entity.getMessage());
        dto.setSentAt(entity.getSentAt());

        return dto;
    }
}
