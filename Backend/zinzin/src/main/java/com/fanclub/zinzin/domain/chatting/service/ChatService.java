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

    @Transactional
    public void saveChatMessage(Long roomId, RequestMessageDto chat) {
        chatMessageRepository.save(new ChatMessage(roomId, chat.getMemberId(), chat.getMessage()));
    }

    public String getLastMessage(Long roomId) {
        Optional<ChatMessage> chatMessage = chatMessageRepository.findTop1ByRoomIdOrderByTimestampDesc(roomId);

        return chatMessage.map(ChatMessage::getMessage).orElse(null);
    }

}
