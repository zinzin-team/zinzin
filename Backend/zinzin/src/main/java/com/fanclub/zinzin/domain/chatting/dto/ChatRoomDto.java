package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ChatRoomDto {

    private Long roomId;
    private ChatRoomType roomType;
    private LocalDate lastMessageDate;
    private MemberChatDto otherMember;
    private String lastMessage;

    public static ChatRoomDto fromEntity(ChatRoom chatRoom, Long memberId) {
        MemberChatDto otherMember = chatRoom.getMembers().stream()
                .map(member -> MemberChatDto.fromEntity(member.getMemberInfo()))
                .filter(memberDto -> !memberDto.getMemberId().equals(memberId))
                .findFirst().orElse(null);

        String lastMessage = chatRoom.getLastMessageId(); // MongoDB에서 lastMessageId에 대한 메시지를 가져온다.

        return ChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .roomType(chatRoom.getRoomType())
                .lastMessage(chatRoom.getLastMessageId())
                .lastMessageDate(chatRoom.getLastMessageDate())
                .otherMember(otherMember)
                .build();
    }
}
