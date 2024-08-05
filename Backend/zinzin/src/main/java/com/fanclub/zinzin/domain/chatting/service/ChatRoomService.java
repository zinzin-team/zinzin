package com.fanclub.zinzin.domain.chatting.service;

import com.fanclub.zinzin.domain.chatting.dto.ChatRoomDto;
import com.fanclub.zinzin.domain.chatting.dto.ResponseMessageDto;
import com.fanclub.zinzin.domain.chatting.entity.ChatMessage;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomStatus;
import com.fanclub.zinzin.domain.chatting.repository.ChatMessageRepository;
import com.fanclub.zinzin.domain.chatting.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatRoomDto> getChatRoomsByMemberId(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberIdAndStatus(memberId, ChatRoomStatus.ACTIVE);
        return convertToDto(chatRooms, memberId);
    }

    private List<ChatRoomDto> convertToDto(List<ChatRoom> chatRooms, Long memberId) {
        return chatRooms.stream()
                .map(chatRoom -> ChatRoomDto.fromEntity(chatRoom, memberId))
                .collect(Collectors.toList());
    }

    public List<ResponseMessageDto> getRoomMessages(Long roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(roomId);
        List<ResponseMessageDto> result = new ArrayList<>();
        for(ChatMessage chatMessage : chatMessages) {
            result.add(ResponseMessageDto.of(chatMessage));
        }
        return result;
    }

}
