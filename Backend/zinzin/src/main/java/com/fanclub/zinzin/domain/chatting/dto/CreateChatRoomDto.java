package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.chatting.entity.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateChatRoomDto {

    private final ChatRoomType roomType;
    private final Long myMemberId;
    private final Long otherMemberId;
}
