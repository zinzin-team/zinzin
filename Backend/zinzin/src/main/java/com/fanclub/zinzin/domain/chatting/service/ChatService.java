package com.fanclub.zinzin.domain.chatting.service;

import com.fanclub.zinzin.domain.chatting.dto.RequestMessageDto;
import com.fanclub.zinzin.domain.chatting.entity.ChatMessage;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.repository.ChatMessageRepository;
import com.fanclub.zinzin.domain.chatting.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void saveChatMessage(RequestMessageDto chat) {
        log.info("ChatService.saveChatMessage() is called.");
        log.info("ChatRequest={}", chat.toString());
        chatMessageRepository.save(new ChatMessage(chat.getRoomId(), chat.getMemberId(), chat.getMessage()));
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(chat.getRoomId());
        for (ChatMessage chatMessage : chatMessages) {
            System.out.println("roomId: " + chatMessage.getRoomId() + "memberId: " + chatMessage.getMemberId() + "message: " + chatMessage.getMessage());
        }
    }

    public String getLastMessage(Long roomId) {
        log.info("getLastMessage() is called.");
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(roomId);

        if (chatRoomOpt.isPresent()) {
            ChatRoom chatRoom = chatRoomOpt.get();
            String lastMessageId = chatRoom.getLastMessageId();
            if (lastMessageId != null) {
                Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findById(lastMessageId);
                if (lastMessageOpt.isPresent()) {
                    return lastMessageOpt.get().getMessage();
                }
            }
        }
        return null;
    }

}
