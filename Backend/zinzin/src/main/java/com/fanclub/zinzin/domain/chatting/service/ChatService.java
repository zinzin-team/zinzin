package com.fanclub.zinzin.domain.chatting.service;

import com.fanclub.zinzin.domain.chatting.dto.ChatRoomDto;
import com.fanclub.zinzin.domain.chatting.dto.RequestMessageDto;
import com.fanclub.zinzin.domain.chatting.dto.ResponseMessageDto;
import com.fanclub.zinzin.domain.chatting.entity.ChatMessage;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomStatus;
import com.fanclub.zinzin.domain.chatting.repository.ChatMessageRepository;
import com.fanclub.zinzin.domain.chatting.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMessage saveChatMessage(RequestMessageDto chat) {
        log.info("ChatService.saveChatMessage() is called.");
        log.info("ChatRequest={}", chat.toString());
        return chatMessageRepository.save(
                new ChatMessage(chat.getRoomId(), chat.getMemberId(), chat.getContent())
        );
    }
}
