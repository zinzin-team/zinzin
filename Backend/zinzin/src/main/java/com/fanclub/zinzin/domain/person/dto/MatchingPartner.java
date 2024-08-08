package com.fanclub.zinzin.domain.person.dto;

import com.fanclub.zinzin.domain.member.entity.Gender;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MatchingPartner {
    private final Long memberId;
    private final LocalDate birth;
    private final Gender gender;
    private final Long cardId;
    private final String nickname;

    public MatchingPartner(Long memberId, LocalDate birth, Gender gender, Long cardId, String nickname) {
        this.birth = birth;
        this.memberId = memberId;
        this.gender = gender;
        this.cardId = cardId;
        this.nickname = nickname;
    }
}
