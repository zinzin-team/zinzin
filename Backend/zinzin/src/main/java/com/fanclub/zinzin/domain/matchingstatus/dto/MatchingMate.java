package com.fanclub.zinzin.domain.matchingstatus.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MatchingMate {
    private final String name;
    private final String profileImage;

    @Builder
    private MatchingMate(String name, String profileImage){
        this.name = name;
        this.profileImage = profileImage;
    }

    public static MatchingMate of(String name, String profileImage){
        return MatchingMate.builder()
                .name(name)
                .profileImage(profileImage)
                .build();
    }
}
