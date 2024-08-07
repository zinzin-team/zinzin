package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomMember;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ResponseChatRoomDto {

    private final Long roomId;
    private final ChatRoomType roomType;
    private final LocalDateTime lastMessageDate;
    private final ChatMemberDto otherMember;
    private final String lastMessage;
    private final boolean heartToggle;

    public static ResponseChatRoomDto fromEntity(ChatRoom chatRoom, Long memberId, String lastMessage) {

        ChatMemberDto otherMember = chatRoom.getMembers().stream()
                .filter(member -> !member.getMemberInfo().getMember().getId().equals(memberId))
                .map(member -> ChatMemberDto.fromEntity(member.getMemberInfo()))
                .findFirst().orElse(null);

        boolean heartToggle = chatRoom.getMembers().stream()
                .filter(member -> member.getMemberInfo().getMember().getId().equals(memberId))
                .map(ChatRoomMember::getHeartToggle)
                .findFirst().orElse(false);

        return ResponseChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .roomType(chatRoom.getRoomType())
                .lastMessage(lastMessage)
                .lastMessageDate(chatRoom.getLastMessageDateTime())
                .otherMember(otherMember)
                .heartToggle(heartToggle)
                .build();
    }
}
