package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.chatting.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseMessageDto {

    private final Long roomId;
    private final Long memberId;
    private final String message;

    public static ResponseMessageDto of(ChatMessage chatMessage) {
        return new ResponseMessageDto(
                chatMessage.getRoomId(),
                chatMessage.getMemberId(),
                chatMessage.getMessage()
        );
    }
}
