package com.fanclub.zinzin.domain.matching.dto;

import com.fanclub.zinzin.domain.member.entity.Gender;
import com.fanclub.zinzin.domain.person.dto.MatchingPartner;
import com.fanclub.zinzin.domain.person.dto.Mate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;


@Getter
public class Matching {
    private final int position;
    private final boolean checked;
    private final Long memberId;
    private final LocalDate birth;
    private final String nickname;
    private final Gender gender;
    private final CardInfo card;
    private final List<Mate> mates;

    @Builder
    private Matching(Long memberId, LocalDate birth, Gender gender, CardInfo card, List<Mate> mates, String nickname, int position, boolean checked) {
        this.position = position;
        this.checked = checked;
        this.memberId = memberId;
        this.birth = birth;
        this.nickname = nickname;
        this.gender = gender;
        this.card = card;
        this.mates = mates;
    }

    public static Matching of(MatchingPartner matchingPartner, CardInfo card, List<Mate> mates, int position, boolean checked){
        return Matching.builder()
                .memberId(matchingPartner.getMemberId())
                .birth(matchingPartner.getBirth())
                .gender(matchingPartner.getGender())
                .card(card)
                .mates(mates)
                .nickname(matchingPartner.getNickname())
                .position(position)
                .checked(checked)
                .build();
    }
}
