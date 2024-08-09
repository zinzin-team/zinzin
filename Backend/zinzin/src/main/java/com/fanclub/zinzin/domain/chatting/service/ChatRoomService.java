package com.fanclub.zinzin.domain.chatting.service;

import com.fanclub.zinzin.domain.chatting.dto.CreateChatRoomDto;
import com.fanclub.zinzin.domain.chatting.dto.ResponseChatRoomDto;
import com.fanclub.zinzin.domain.chatting.dto.ResponseMessageDto;
import com.fanclub.zinzin.domain.chatting.entity.ChatMessage;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomMember;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomStatus;
import com.fanclub.zinzin.domain.chatting.repository.ChatMessageRepository;
import com.fanclub.zinzin.domain.chatting.repository.ChatRoomMemberRepository;
import com.fanclub.zinzin.domain.chatting.repository.ChatRoomRepository;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.global.error.code.ChatRoomErrorCode;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatService chatService;
    private final MemberInfoRepository memberInfoRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public List<ResponseChatRoomDto> getChatRoomsByMemberId(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberIdAndStatus(memberId, ChatRoomStatus.ACTIVE);
        chatRooms.sort(Comparator.comparing((ChatRoom chatRoom) -> {
            if (chatRoom == null) {
                return LocalDateTime.MIN;
            } else {
                Long chatRoomId = chatRoom.getId();
                Optional<ChatMessage> lastMessage = chatMessageRepository.findTop1ByRoomIdOrderByTimestampDesc(chatRoomId);
                return lastMessage.map(ChatMessage::getTimestamp).orElse(LocalDateTime.MIN);
            }
        }).reversed());
        return convertToDto(chatRooms, memberId);
    }

    private List<ResponseChatRoomDto> convertToDto(List<ChatRoom> chatRooms, Long memberId) {
        return chatRooms.stream()
                .map(chatRoom -> ResponseChatRoomDto.fromEntity(chatRoom, memberId, chatService.getLastMessage(chatRoom.getId())))
                .collect(Collectors.toList());
    }

    public List<ResponseMessageDto> getRoomMessages(Long roomId, Long memberId) {
        if (!chatRoomMemberRepository.existsByChatRoomIdAndMemberId(roomId, memberId)) {
            throw new BaseException(ChatRoomErrorCode.CANNOT_GET_MESSAGES);
        }
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(roomId);
        List<ResponseMessageDto> result = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            result.add(ResponseMessageDto.of(chatMessage));
        }
        return result;
    }

    @Transactional
    public void createChatRoom(CreateChatRoomDto createChatRoomDto) {
        ChatRoom chatRoom = ChatRoom.createRoom(createChatRoomDto);
        chatRoomRepository.save(chatRoom);

        List<ChatRoomMember> chatRoomMembers = createChatRoomDto.getMemberIds().stream()
                .map(memberId -> {
                    MemberInfo memberInfo = memberInfoRepository.findById(memberId)
                            .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

                    return ChatRoomMember.builder()
                            .chatRoom(chatRoom)
                            .memberInfo(memberInfo)
                            .build();
                }).toList();
        chatRoomMemberRepository.saveAll(chatRoomMembers);
    }

    @Transactional
    public void deleteChatRoom(Long roomId, Long memberId) {
        if (!chatRoomMemberRepository.existsByChatRoomIdAndMemberId(roomId, memberId)) {
            throw new BaseException(ChatRoomErrorCode.CHAT_ROOM_CANNOT_DELETE);
        }

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND));
        chatRoomRepository.delete(chatRoom);
    }

    public ResponseChatRoomDto getChatRoomBetweenMembers(List<Long> memberIds) {
        Long myMemberId = memberIds.get(0);
        Long otherMemberId = memberIds.get(1);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findChatRoomByMemberIds(myMemberId, otherMemberId, ChatRoomStatus.ACTIVE);

        return chatRoom.map(room ->
                        ResponseChatRoomDto.fromEntity(room, myMemberId, chatService.getLastMessage(room.getId())))
                .orElse(null);
    }

    @Transactional
    public void updateHeartToggle(Long memberId, Long roomId, boolean isHeart) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(roomId, memberId)
                .orElseThrow(() -> new BaseException(ChatRoomErrorCode.CHAT_ROOM_CANNOT_DELETE));

        chatRoomMember.updateHeart(isHeart);
        chatRoomMemberRepository.save(chatRoomMember);
    }
}

