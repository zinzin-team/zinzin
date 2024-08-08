package com.fanclub.zinzin.domain.chatting.controller;

import com.fanclub.zinzin.domain.chatting.dto.RequestMessageDto;
import com.fanclub.zinzin.domain.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    public void receiveMessage(@DestinationVariable Long roomId, RequestMessageDto chat) {
        chatService.saveChatMessage(roomId, chat);

        messagingTemplate.convertAndSend("/queue/chatroom/" + roomId, chat);
    }
}
