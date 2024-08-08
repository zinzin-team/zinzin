package com.fanclub.zinzin.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoFriendResponse {
    private final Long memberId;
    private final String profileImage;
    private final String kakaoName;
    private final String relationship;

    @Builder
    private KakaoFriendResponse(Long memberId, String profileImage, String kakaoName, String relationship){
        this.memberId = memberId;
        this.profileImage = profileImage;
        this.kakaoName = kakaoName;
        this.relationship = relationship;
    }

    public static KakaoFriendResponse of(Long memberId, String profileImage, String kakaoName, String relationship){
        return KakaoFriendResponse.builder()
                .memberId(memberId)
                .profileImage(profileImage)
                .kakaoName(kakaoName)
                .relationship(relationship)
                .build();
    }
}
