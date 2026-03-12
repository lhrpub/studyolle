package com.studyolle.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {

    private Long studyId;
    private Long senderId;
    private String senderName;
    private String message;
}
