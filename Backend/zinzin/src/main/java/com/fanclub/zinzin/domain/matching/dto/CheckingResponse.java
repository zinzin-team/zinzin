package com.fanclub.zinzin.domain.matching.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CheckingResponse {
    private final boolean matchingSuccess;
    // 채팅방 관련 멤버 변수 필요

    @Builder
    private CheckingResponse(boolean matchingSuccess){
        this.matchingSuccess = matchingSuccess;
    }

    public static CheckingResponse of(boolean matchingSuccess){
        return CheckingResponse.builder()
                .matchingSuccess(matchingSuccess)
                .build();
    }
}
