package com.fanclub.zinzin.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class RequestMessageDto {
    private final Long memberId;
    private final Long roomId;
    private final String message;

}
