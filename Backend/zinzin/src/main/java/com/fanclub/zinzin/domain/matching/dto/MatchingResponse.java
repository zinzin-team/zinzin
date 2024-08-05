package com.fanclub.zinzin.domain.matching.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchingResponse {
    private final int totalCnt;
    private final List<Matching> matchings;

    @Builder
    private MatchingResponse(List<Matching> matchings){
        this.totalCnt = matchings.size();
        this.matchings = matchings;
    }

    public static MatchingResponse of(List<Matching> matchings){
        return MatchingResponse.builder()
                .matchings(matchings)
                .build();
    }
}
