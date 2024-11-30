package com.fanclub.zinzin.domain.matching.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchingResponse {
    private final int totalCnt;
    private final Set<Matching> matchings;

    @Builder
    private MatchingResponse(Set<Matching> matchings){
        this.totalCnt = matchings.size();
        this.matchings = matchings;
    }

    public static MatchingResponse of(Set<Matching> matchings){
        return MatchingResponse.builder()
                .matchings(matchings)
                .build();
    }
}
