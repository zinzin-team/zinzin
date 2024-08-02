package com.fanclub.zinzin.domain.mathcing.dto;

import com.fanclub.zinzin.domain.member.entity.Gender;
import lombok.Getter;

@Getter
public class MatchingPartner {
    private final Long memberId;
    private final Gender gender;
    private final Long cardId;
    private final String nickname;

    public MatchingPartner(Long memberId, Gender gender, Long cardId, String nickname) {
        this.memberId = memberId;
        this.gender = gender;
        this.cardId = cardId;
        this.nickname = nickname;
    }
}
