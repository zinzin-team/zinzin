package com.fanclub.zinzin.domain.matching.dto;

import com.fanclub.zinzin.domain.chatting.dto.ResponseChatRoomDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CheckingResponse {
    private final boolean matchingSuccess;
    private final ResponseChatRoomDto responseChatRoomDto;

    @Builder
    private CheckingResponse(boolean matchingSuccess, ResponseChatRoomDto responseChatRoomDto){
        this.matchingSuccess = matchingSuccess;
        this.responseChatRoomDto = responseChatRoomDto;
    }

    public static CheckingResponse of(boolean matchingSuccess){
        return CheckingResponse.builder()
                .matchingSuccess(matchingSuccess)
                .build();
    }

    public static CheckingResponse of(ResponseChatRoomDto responseChatRoomDto, boolean matchingSuccess){
        return CheckingResponse.builder()
                .responseChatRoomDto(responseChatRoomDto)
                .matchingSuccess(matchingSuccess)
                .build();
    }
}
