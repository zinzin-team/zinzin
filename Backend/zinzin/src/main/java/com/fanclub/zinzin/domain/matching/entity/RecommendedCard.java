package com.fanclub.zinzin.domain.matching.entity;

import com.fanclub.zinzin.domain.card.entity.Card;
import com.fanclub.zinzin.domain.matching.dto.CardInfo;
import com.fanclub.zinzin.domain.matching.dto.Matching;
import com.fanclub.zinzin.domain.member.entity.Member;
import com.fanclub.zinzin.domain.person.dto.MatchingPartner;
import com.fanclub.zinzin.domain.person.dto.Mate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommended_card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recommended_date")
    private LocalDate recommendedDate;

    @Column(nullable = false)
    private Integer position;

    @Getter
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean checked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    @Getter
    private Card card;

    @Builder
    private RecommendedCard(LocalDate recommendedDate, Integer position, Boolean checked, Member member, Card card){
        this.recommendedDate = recommendedDate;
        this.position = position;
        this.checked = checked;
        this.member = member;
        this.card =card;
    }

    public static RecommendedCard of(int position, boolean checked, Member member, Card card) {
        return RecommendedCard.builder()
                .recommendedDate(LocalDate.now())
                .position(position)
                .checked(checked)
                .member(member)
                .card(card)
                .build();
    }

    public Matching toMatching(MatchingPartner matchingPartner, List<Mate> mates){
        return Matching.of(matchingPartner, CardInfo.of(this.card), mates, this.position, this.checked);
    }

    public void checkCard(){
        this.checked = true;
    }
}
