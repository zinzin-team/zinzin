package com.fanclub.zinzin.domain.chatting.service;

import com.fanclub.zinzin.domain.chatting.dto.CreateChatRoomDto;
import com.fanclub.zinzin.domain.chatting.dto.HeartToggleDto;
import com.fanclub.zinzin.domain.chatting.dto.ResponseChatRoomDto;
import com.fanclub.zinzin.domain.chatting.dto.ResponseMessageDto;
import com.fanclub.zinzin.domain.chatting.entity.*;
import com.fanclub.zinzin.domain.chatting.repository.ChatMessageRepository;
import com.fanclub.zinzin.domain.chatting.repository.ChatRoomMemberRepository;
import com.fanclub.zinzin.domain.chatting.repository.ChatRoomRepository;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.repository.MemberInfoRepository;
import com.fanclub.zinzin.domain.member.repository.MemberRepository;
import com.fanclub.zinzin.domain.person.entity.Person;
import com.fanclub.zinzin.domain.person.repository.PersonRepository;
import com.fanclub.zinzin.global.error.code.ChatRoomErrorCode;
import com.fanclub.zinzin.global.error.code.MemberErrorCode;
import com.fanclub.zinzin.global.error.exception.BaseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatService chatService;
    private final MemberInfoRepository memberInfoRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final PersonRepository personRepository;
    private final MemberRepository memberRepository;

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
        ArrayList<ResponseChatRoomDto> responseChatRoomDtoList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            List<ChatRoomMember> chatRoomMembers = chatRoom.getMembers();

            ChatRoomMember me = null;
            ChatRoomMember otherMember = null;
            if (chatRoomMembers.get(0).getMember().getId().equals(memberId)) {
                me = chatRoomMembers.get(0);
                otherMember = chatRoomMembers.get(1);
            } else {
                me = chatRoomMembers.get(1);
                otherMember = chatRoomMembers.get(0);
            }

            Person otherPerson = personRepository.findPersonByMemberId(otherMember.getMember().getId())
                    .orElse(null);

            if (otherPerson == null) {
                log.error("ChatRoomService.convertToDto >> Neo4j에서 otherMember 찾기 실패");
                continue;
            }

            boolean heartToggle = me.getHeartToggle();
            responseChatRoomDtoList.add(ResponseChatRoomDto.of(chatRoom, otherPerson, chatService.getLastMessage(chatRoom.getId()), heartToggle));
        }

        return responseChatRoomDtoList;
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
    public ResponseChatRoomDto createAndFetchChatRoom(CreateChatRoomDto createChatRoomDto, Long myMemberId) {
        ChatRoom chatRoom = ChatRoom.createRoom(createChatRoomDto);
        chatRoomRepository.save(chatRoom);

        List<ChatRoomMember> chatRoomMembers = new ArrayList<>();
        chatRoomMembers.add(new ChatRoomMember(chatRoom, memberRepository.findById(myMemberId)
                .orElseThrow(()-> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND))));
        chatRoomMembers.add(new ChatRoomMember(chatRoom, memberRepository.findById(createChatRoomDto.getTargetId())
                .orElseThrow(()-> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND))));

        chatRoom.updateMembers(chatRoomMembers);

        Long otherMemberId = createChatRoomDto.getTargetId();
        List<ChatRoom> fetchedChatRoomList = chatRoomRepository.findChatRoomByMemberIds(myMemberId, otherMemberId, ChatRoomStatus.ACTIVE);

        if(fetchedChatRoomList.isEmpty()){
            throw new BaseException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND);
        }

        if(fetchedChatRoomList.size() > 1){
            throw new BaseException(ChatRoomErrorCode.EXISTED_CHAT_ROOM);
        }

        ChatRoom fetchedChatRoom = fetchedChatRoomList.get(0);
        Person otherPerson = personRepository.findPersonByMemberId(otherMemberId)
                .orElseThrow(() -> new BaseException(MemberErrorCode.MEMBER_NOT_FOUND));

        boolean heartToggle = chatRoomMembers.stream()
                .filter(member -> member.getMember().getId().equals(myMemberId))
                .map(ChatRoomMember::getHeartToggle)
                .findFirst().orElse(false);

        return ResponseChatRoomDto.of(fetchedChatRoom, otherPerson, chatService.getLastMessage(fetchedChatRoom.getId()), heartToggle);
    }

    @Transactional
    public void deleteChatRoom(Long roomId, Long memberId) {
        if (!chatRoomMemberRepository.existsByChatRoomIdAndMemberId(roomId, memberId)) {
            throw new BaseException(ChatRoomErrorCode.CHAT_ROOM_CANNOT_DELETE);
        }

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND));
        chatRoomRepository.delete(chatRoom);

        if(chatRoom.getRoomType() == ChatRoomType.LIKE){
            ChatRoomMember firstMember = chatRoom.getMembers().get(0);
            ChatRoomMember secondMember = chatRoom.getMembers().get(1);
            if (firstMember.getMember().getId().equals(memberId)) {
                personRepository.exitChatroom(firstMember.getMember().getId(), secondMember.getMember().getId());
            } else {
                personRepository.exitChatroom(secondMember.getMember().getId(), firstMember.getMember().getId());
            }
        }
    }

    @Transactional
    public HeartToggleDto updateHeartToggle(Long memberId, Long roomId, boolean isHeart) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(ChatRoomErrorCode.CHAT_ROOM_NOT_FOUND));

        ChatRoomMember myMember = null;
        ChatRoomMember otherMember = null;

        for (ChatRoomMember member : chatRoom.getMembers()) {
            if (member.getMember().getId().equals(memberId)) {
                myMember = member;
            } else {
                otherMember = member;
            }
        }
        if (myMember == null || otherMember == null) {
            throw new BaseException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        myMember.updateHeart(isHeart);
        if (isHeart && otherMember.getHeartToggle()) {
            chatRoom.updateChatRoomType(ChatRoomType.LOVE);
            return new HeartToggleDto(true);
        }
        return new HeartToggleDto(false);
    }
}

