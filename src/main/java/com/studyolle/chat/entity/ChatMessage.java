package com.studyolle.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studyId;

    private Long senderId;

    private String senderName;

    @Lob
    private String message;

    private LocalDateTime sentAt;
}
