package com.fanclub.zinzin.domain.mathcing.dto;

import com.fanclub.zinzin.domain.member.entity.Gender;
import com.fanclub.zinzin.domain.person.dto.MatchingPartner;
import com.fanclub.zinzin.domain.person.dto.Mate;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class Matching {
    private final int order;
    private final boolean checked;
    private final Long memberId;
    private final String nickname;
    private final Gender gender;
    private final CardInfo card;
    private final List<Mate> mates;

    @Builder
    private Matching(Long memberId, Gender gender, CardInfo card, List<Mate> mates, String nickname, int order, boolean checked) {
        this.order = order;
        this.checked = checked;
        this.memberId = memberId;
        this.nickname = nickname;
        this.gender = gender;
        this.card = card;
        this.mates = mates;
    }

    public static Matching of(MatchingPartner matchingPartner, CardInfo card, List<Mate> mates, int order, boolean checked){
        return Matching.builder()
                .memberId(matchingPartner.getMemberId())
                .gender(matchingPartner.getGender())
                .card(card)
                .mates(mates)
                .nickname(matchingPartner.getNickname())
                .order(order)
                .checked(checked)
                .build();
    }
}
