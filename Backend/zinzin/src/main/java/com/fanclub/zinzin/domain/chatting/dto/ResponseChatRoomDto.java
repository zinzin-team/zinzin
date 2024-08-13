package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomType;
import com.fanclub.zinzin.domain.person.entity.Person;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseChatRoomDto {

    private final Long roomId;
    private final ChatRoomType roomType;
    private final ChatMemberDto otherMember;
    private final String lastMessage;
    private final boolean heartToggle;

    @Builder
    private ResponseChatRoomDto(Long roomId, ChatRoomType roomType, ChatMemberDto otherMember, String lastMessage, boolean heartToggle){
        this.roomId = roomId;
        this.roomType = roomType;
        this.otherMember = otherMember;
        this.lastMessage = lastMessage;
        this.heartToggle = heartToggle;
    }

    public static ResponseChatRoomDto of(ChatRoom chatRoom, Person otherMember, String lastMessage, boolean heartToggle){
        return ResponseChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .roomType(chatRoom.getRoomType())
                .otherMember(ChatMemberDto.fromEntity(otherMember))
                .heartToggle(heartToggle)
                .lastMessage(lastMessage)
                .build();
    }
}
