package com.fanclub.zinzin.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CheckSearchIdResponse {
    private final boolean duplicated;

    @Builder
    private CheckSearchIdResponse(boolean duplicated){
        this.duplicated = duplicated;
    }

    public static CheckSearchIdResponse of(boolean duplicated){
        return CheckSearchIdResponse.builder()
                .duplicated(duplicated)
                .build();
    }
}
