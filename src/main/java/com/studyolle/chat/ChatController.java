package com.studyolle.chat;

import com.studyolle.chat.dto.ChatMessageDTO;
import com.studyolle.chat.entity.ChatMessage;
import com.studyolle.chat.repository.ChatMessageRepository;
import com.studyolle.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
    private final ChatService chatService;
    private final ModelMapper modelMapper;

    @MessageMapping("/chat.send")
    public void send(ChatMessageDTO dto){
        ChatMessage chatMessage = modelMapper.map(dto, ChatMessage.class);
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        template.convertAndSend("/topic/study/" + dto.getStudyId(), savedMessage);
    }

//    @GetMapping("/api/chat/{studyId}")
//    @ResponseBody
//    public List<ChatMessage> getMessages(@PathVariable Long studyId){
//        return chatMessageRepository.findByStudyIdOrderBySentAtAsc(studyId);
//    }

    @GetMapping("/api/chat/{studyId}")
    @ResponseBody
    public Slice<ChatMessageDTO> getMessages(@PathVariable Long studyId ,
                                          @PageableDefault(size = 20)Pageable pageable){
        return chatService.getChatMessages(studyId, pageable);
    }
}
