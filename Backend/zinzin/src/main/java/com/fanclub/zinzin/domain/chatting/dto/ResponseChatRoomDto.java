package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoom;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomMember;
import com.fanclub.zinzin.domain.chatting.entity.ChatRoomType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Getter
@Builder
public class ResponseChatRoomDto {

    private final Long roomId;
    private final ChatRoomType roomType;
    private final ChatMemberDto otherMember;
    private final String lastMessage;
    private final boolean heartToggle;

    public static ResponseChatRoomDto fromEntity(ChatRoom chatRoom, Long memberId, String lastMessage) {
        List<ChatRoomMember> members = chatRoom.getMembers();
        if(members == null || members.size() == 0) {
            log.info("members가 없습니다.");
        }
        log.info("members가 있습니다.");
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
                .otherMember(otherMember)
                .heartToggle(heartToggle)
                .build();
    }
}
