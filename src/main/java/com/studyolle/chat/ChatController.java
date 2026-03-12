package com.studyolle.chat;

import com.studyolle.chat.dto.ChatMessageDTO;
import com.studyolle.chat.entity.ChatMessage;
import com.studyolle.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatMessageRepository chatMessageRepository;
    private final ModelMapper modelMapper;

    @MessageMapping("/chat.send")
    public void send(ChatMessageDTO dto){
        ChatMessage chatMessage = modelMapper.map(dto, ChatMessage.class);
        chatMessageRepository.save(chatMessage);
        template.convertAndSend("/topic/study/" + dto.getStudyId(), dto);
    }

    @GetMapping("/api/chat/{studyId}")
    @ResponseBody
    public List<ChatMessage> getMessages(@PathVariable Long studyId){
        return chatMessageRepository.findByStudyIdOrderBySentAtAsc(studyId);
    }
}
