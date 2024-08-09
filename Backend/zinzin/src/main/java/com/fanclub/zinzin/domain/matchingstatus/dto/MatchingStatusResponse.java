package com.fanclub.zinzin.domain.matchingstatus.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchingStatusResponse {
    private final MatchingMate mate1;
    private final MatchingMate mate2;

    @Builder
    private MatchingStatusResponse(MatchingMate mate1, MatchingMate mate2){
        this.mate1 = mate1;
        this.mate2 = mate2;
    }

    public static MatchingStatusResponse of(MatchingStatus matchingStatus){
        return MatchingStatusResponse.builder()
                .mate1(matchingStatus.getMate1().toMatchingMate())
                .mate2(matchingStatus.getMate2().toMatchingMate())
                .build();
    }

}
