package com.fanclub.zinzin.domain.member.dto;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.member.entity.Gender;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.member.entity.MemberInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class MemberInfoResponse {
    private final String email;
    private final String name;
    private final String nickname;
    private final LocalDate birth;
    private final Gender gender;
    private final String profileImage;
    private final String searchId;
    private final boolean matchingMode;
    private final LocalDateTime matchingModeLog;
    private final boolean hasCard;
    private final SimpleCardInfo card;

    @Builder
    private MemberInfoResponse(String email, String name, String nickname, LocalDate birth, Gender gender, String profileImage, String searchId, boolean matchingMode, LocalDateTime matchingModeLog, boolean hasCard, SimpleCardInfo card) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.profileImage = profileImage;
        this.searchId = searchId;
        this.matchingMode = matchingMode;
        this.matchingModeLog = matchingModeLog;
        this.hasCard = hasCard;
        this.card = card;
    }

    public static MemberInfoResponse of(MemberInfo memberInfo, Card card){
        Member member = memberInfo.getMember();
        SimpleCardInfo simpleCardInfo = (card == null)?null:SimpleCardInfo.of(card);
        return MemberInfoResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(memberInfo.getNickname())
                .birth(member.getBirth())
                .gender(member.getGender())
                .profileImage(memberInfo.getProfileImage())
                .searchId(memberInfo.getSearchId())
                .matchingMode(memberInfo.getMatchingMode())
                .matchingModeLog(memberInfo.getMatchingModeLog())
                .hasCard(card != null)
                .card(simpleCardInfo)
                .build();
    }
}
