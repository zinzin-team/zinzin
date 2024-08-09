package com.fanclub.zinzin.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageDto {

    private final Long chatId;
    private final Long roomId;
    private final String message;

}
