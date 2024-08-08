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
        chatMessageRepository.save(new ChatMessage(chat.getRoomId(), chat.getMemberId(), chat.getMessage()));
    }

    public String getLastMessage(Long roomId) {
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
