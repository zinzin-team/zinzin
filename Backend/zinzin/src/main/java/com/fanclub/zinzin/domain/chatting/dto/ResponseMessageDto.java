package com.fanclub.zinzin.domain.chatting.dto;

import com.fanclub.zinzin.domain.chatting.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessageDto {

    private Long roomId;
    private Long memberId;
    private String message;

    public static ResponseMessageDto of(ChatMessage chatMessage) {
        return new ResponseMessageDto(
                chatMessage.getRoomId(),
                chatMessage.getMemberId(),
                chatMessage.getMessage()
        );
    }
}
