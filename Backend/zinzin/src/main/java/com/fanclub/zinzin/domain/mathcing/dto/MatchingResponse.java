package com.fanclub.zinzin.domain.mathcing.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class MatchingResponse {
    private final List<MatchingPartner> matchingPartners;

    @Builder
    private MatchingResponse(List<MatchingPartner> matchingPartners){
        this.matchingPartners = matchingPartners;
    }

    public static MatchingResponse of(List<MatchingPartner> matchingPartners){
        return MatchingResponse.builder()
                .matchingPartners(matchingPartners)
                .build();
    }
}
