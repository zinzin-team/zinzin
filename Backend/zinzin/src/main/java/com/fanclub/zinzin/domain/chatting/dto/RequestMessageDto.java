package com.fanclub.zinzin.domain.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessageDto {
    private Long memberId;
    private Long roomId;
    private String content;

}
