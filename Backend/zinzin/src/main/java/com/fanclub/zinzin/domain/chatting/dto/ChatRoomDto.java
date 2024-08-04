package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ChatRoomDto {

    private Long roomId;
    private ChatRoomType roomType;
    private LocalDate lastMessageDate;
    private List<MemberChatDto> members;

    public static ChatRoomDto fromEntity(ChatRoom chatRoom, Long memberId) {
        List<MemberChatDto> memberChatDtos = chatRoom.getMembers().stream()
                .map(member -> MemberChatDto.fromEntity(member.getMemberInfo()))
                .filter(memberDto -> !memberDto.getMemberId().equals(memberId))
                .toList();

        return ChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .roomType(chatRoom.getRoomType())
                .lastMessageDate(chatRoom.getLastMessageDate())
                .members(memberChatDtos)
                .build();
    }
}
