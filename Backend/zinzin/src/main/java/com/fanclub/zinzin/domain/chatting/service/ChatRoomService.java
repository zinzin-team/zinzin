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
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.transaction.Transactional;
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
    private final ChatService chatService;
    private final MemberInfoRepository memberInfoRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public List<ResponseChatRoomDto> getChatRoomsByMemberId(Long memberId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberIdAndStatusOrderByLastMessageDateDesc(memberId, ChatRoomStatus.ACTIVE);
        return convertToDto(chatRooms, memberId);
    }

    private List<ResponseChatRoomDto> convertToDto(List<ChatRoom> chatRooms, Long memberId) {
        return chatRooms.stream()
                .map(chatRoom -> ResponseChatRoomDto.fromEntity(chatRoom, memberId, chatService.getLastMessage(chatRoom.getId())))
                .collect(Collectors.toList());
    }

    public List<ResponseMessageDto> getRoomMessages(Long roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(roomId);
        List<ResponseMessageDto> result = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            result.add(ResponseMessageDto.of(chatMessage));
        }
        return result;
    }

    @Transactional
    public void createChatRoom(CreateChatRoomDto createChatRoomDto) {
        MemberInfo myMemberInfo = memberInfoRepository.findById(createChatRoomDto.getMyMemberId())
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));
        MemberInfo otherMemberInfo = memberInfoRepository.findById(createChatRoomDto.getOtherMemberId())
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        ChatRoom babyRoom = ChatRoom.createRoom(createChatRoomDto, "");
        chatRoomRepository.save(babyRoom);

        ChatRoomMember chatRoomMember1 = new ChatRoomMember(babyRoom, myMemberInfo);
        ChatRoomMember chatRoomMember2 = new ChatRoomMember(babyRoom, otherMemberInfo);
//
//        chatRoomMemberRepository.save(chatRoomMember1);
//        chatRoomMemberRepository.save(chatRoomMember2);
    }

}
